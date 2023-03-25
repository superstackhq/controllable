package one.superstack.controllable.controller;

import jakarta.validation.Valid;
import one.superstack.controllable.auth.AuthenticatedController;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.exception.NotAllowedException;
import one.superstack.controllable.model.Environment;
import one.superstack.controllable.request.EnvironmentCreationRequest;
import one.superstack.controllable.request.EnvironmentUpdateRequest;
import one.superstack.controllable.service.AccessService;
import one.superstack.controllable.service.EnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class EnvironmentController extends AuthenticatedController {

    private final EnvironmentService environmentService;

    private final AccessService accessService;

    @Autowired
    public EnvironmentController(EnvironmentService environmentService, AccessService accessService) {
        this.environmentService = environmentService;
        this.accessService = accessService;
    }

    @PostMapping(value = "/environments")
    public Environment create(@Valid @RequestBody EnvironmentCreationRequest environmentCreationRequest) {
        if (!accessService.hasPermission(TargetType.ENVIRONMENT, null, getActorType(), getActorId(), Permission.CREATE, hasFullAccess())) {
            throw new NotAllowedException();
        }

        return environmentService.create(environmentCreationRequest, getActor());
    }

    @GetMapping(value = "/environments")
    public List<Environment> list(Pageable pageable) {
        return environmentService.list(getOrganizationId(), pageable);
    }

    @GetMapping(value = "/environments/{environmentId}")
    public Environment get(@PathVariable String environmentId) throws Throwable {
        return environmentService.get(environmentId, getOrganizationId());
    }

    @PutMapping(value = "/environments/{environmentId}")
    public Environment update(@PathVariable String environmentId, @Valid @RequestBody EnvironmentUpdateRequest environmentUpdateRequest) throws Throwable {
        if (!accessService.hasPermission(TargetType.ENVIRONMENT, environmentId, getActorType(), getActorId(), Permission.UPDATE, hasFullAccess())) {
            throw new NotAllowedException();
        }

        return environmentService.update(environmentId, environmentUpdateRequest, getOrganizationId());
    }

    @DeleteMapping(value = "/environments/{environmentId}")
    public Environment delete(@PathVariable String environmentId) throws Throwable {
        if (!accessService.hasPermission(TargetType.ENVIRONMENT, environmentId, getActorType(), getActorId(), Permission.DELETE, hasFullAccess())) {
            throw new NotAllowedException();
        }

        return environmentService.delete(environmentId, getOrganizationId());
    }
}
