package one.superstack.controllable.controller;

import jakarta.validation.Valid;
import one.superstack.controllable.auth.actor.AuthenticatedController;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.model.AppAccess;
import one.superstack.controllable.request.AppAccessRequest;
import one.superstack.controllable.request.DeleteAllAppAccessRequest;
import one.superstack.controllable.response.AppAccessResponse;
import one.superstack.controllable.response.SuccessResponse;
import one.superstack.controllable.response.TargetResponse;
import one.superstack.controllable.service.AccessService;
import one.superstack.controllable.service.AppAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/api/v1")
public class AppAccessController extends AuthenticatedController {

    private final AppAccessService appAccessService;

    private final AccessService accessService;

    @Autowired
    public AppAccessController(AppAccessService appAccessService, AccessService accessService) {
        this.appAccessService = appAccessService;
        this.accessService = accessService;
    }

    @PostMapping(value = "/apps/{appId}/access")
    public SuccessResponse add(@PathVariable String appId, @Valid @RequestBody AppAccessRequest appAccessRequest) {
        checkAccess(accessService, appAccessRequest.getTargetType(), appAccessRequest.getTargetId(), Permission.MANAGE_APP_ACCESS);
        checkAccess(accessService, TargetType.APP, appId, Permission.MANAGE_TARGET_ACCESS);
        appAccessService.add(appId, appAccessRequest, getActor());
        return new SuccessResponse("App access added successfully");
    }

    @DeleteMapping(value = "/apps/{appId}/access")
    public SuccessResponse delete(@PathVariable String appId, @Valid @RequestBody AppAccessRequest appAccessRequest) {
        checkAccess(accessService, appAccessRequest.getTargetType(), appAccessRequest.getTargetId(), Permission.MANAGE_APP_ACCESS);
        checkAccess(accessService, TargetType.APP, appId, Permission.MANAGE_TARGET_ACCESS);
        appAccessService.delete(appId, appAccessRequest, getOrganizationId());
        return new SuccessResponse("App access deleted successfully");
    }

    @DeleteMapping(value = "/apps/{appId}/access/all")
    public AppAccess deleteAll(@PathVariable String appId, @Valid @RequestBody DeleteAllAppAccessRequest deleteAllAppAccessRequest) throws Throwable {
        checkAccess(accessService, deleteAllAppAccessRequest.getTargetType(), deleteAllAppAccessRequest.getTargetId(), Permission.MANAGE_APP_ACCESS);
        checkAccess(accessService, TargetType.APP, appId, Permission.MANAGE_TARGET_ACCESS);
        return appAccessService.deleteAll(appId, deleteAllAppAccessRequest, getOrganizationId());
    }

    @GetMapping(value = "/apps/{appId}/access")
    public List<TargetResponse> listTargets(@PathVariable String appId, @RequestParam TargetType targetType, Pageable pageable) throws ExecutionException, InterruptedException {
        return appAccessService.listTargets(appId, targetType, getOrganizationId(), pageable);
    }

    @GetMapping(value = "/access/apps")
    public List<AppAccessResponse> listApps(@RequestParam TargetType targetType, @RequestParam String targetId, Pageable pageable) {
        return appAccessService.listApps(targetType, targetId, getOrganizationId(), pageable);
    }
}
