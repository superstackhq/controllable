package one.superstack.controllable.controller;

import jakarta.validation.Valid;
import one.superstack.controllable.auth.AuthenticatedController;
import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.model.Access;
import one.superstack.controllable.request.AccessRequest;
import one.superstack.controllable.request.DeleteAllAccessRequest;
import one.superstack.controllable.response.SuccessResponse;
import one.superstack.controllable.service.AccessControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class AccessController extends AuthenticatedController {

    private final AccessControlService accessControlService;

    @Autowired
    public AccessController(AccessControlService accessControlService) {
        this.accessControlService = accessControlService;
    }

    @PostMapping(value = "/access")
    public SuccessResponse add(@Valid @RequestBody AccessRequest accessRequest) {
        checkFullAccess();
        accessControlService.add(accessRequest, getActor());
        return new SuccessResponse("Access added successfully");
    }

    @DeleteMapping(value = "/access")
    public SuccessResponse delete(@Valid @RequestBody AccessRequest accessRequest) {
        checkFullAccess();
        accessControlService.delete(accessRequest, getOrganizationId());
        return new SuccessResponse("Access deleted successfully");
    }

    @DeleteMapping(value = "/access/all")
    public Access deleteAll(@Valid @RequestBody DeleteAllAccessRequest deleteAllAccessRequest) throws Throwable {
        checkFullAccess();
        return accessControlService.deleteAll(deleteAllAccessRequest, getOrganizationId());
    }

    @GetMapping(value = "/access")
    public List<Access> list(@RequestParam TargetType targetType, @RequestParam String targetId, @RequestParam ActorType actorType, @RequestParam(required = false) String actorId, @RequestParam String environmentId, Pageable pageable) throws Throwable {
        if (null == actorId || actorId.isBlank()) {
            return accessControlService.list(targetType, targetId, actorType, environmentId, getOrganizationId(), pageable);
        } else {
            return accessControlService.get(targetType, targetId, actorType, actorId, environmentId, getOrganizationId());
        }
    }
}