package one.superstack.controllable.controller;

import jakarta.validation.Valid;
import one.superstack.controllable.auth.actor.AuthenticatedController;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
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
        checkAccess(accessService, TargetType.ENVIRONMENT, null, Permission.CREATE);
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
        checkAccess(accessService, TargetType.ENVIRONMENT, environmentId, Permission.UPDATE);
        return environmentService.update(environmentId, environmentUpdateRequest, getOrganizationId());
    }

    @DeleteMapping(value = "/environments/{environmentId}")
    public Environment delete(@PathVariable String environmentId) throws Throwable {
        checkAccess(accessService, TargetType.ENVIRONMENT, environmentId, Permission.DELETE);
        return environmentService.delete(environmentId, getActor());
    }
}
