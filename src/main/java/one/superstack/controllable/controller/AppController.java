package one.superstack.controllable.controller;

import jakarta.validation.Valid;
import one.superstack.controllable.auth.actor.AuthenticatedController;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.model.App;
import one.superstack.controllable.request.AppCreationRequest;
import one.superstack.controllable.request.AppUpdateRequest;
import one.superstack.controllable.response.AccessKeyResponse;
import one.superstack.controllable.service.AccessService;
import one.superstack.controllable.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class AppController extends AuthenticatedController {

    private final AppService appService;

    private final AccessService accessService;

    @Autowired
    public AppController(AppService appService, AccessService accessService) {
        this.appService = appService;
        this.accessService = accessService;
    }

    @PostMapping(value = "/apps")
    public App create(@Valid @RequestBody AppCreationRequest appCreationRequest) {
        checkAccess(accessService, TargetType.APP, null, Permission.CREATE);
        return appService.create(appCreationRequest, getActor());
    }

    @GetMapping(value = "/apps")
    public List<App> list(Pageable pageable) {
        return appService.list(getOrganizationId(), pageable);
    }

    @GetMapping(value = "/apps/{appId}")
    public App get(@PathVariable String appId) throws Throwable {
        return appService.get(appId, getOrganizationId());
    }

    @PutMapping(value = "/apps/{appId}")
    public App update(@PathVariable String appId, @Valid @RequestBody AppUpdateRequest appUpdateRequest) throws Throwable {
        checkAccess(accessService, TargetType.APP, appId, Permission.UPDATE);
        return appService.update(appId, appUpdateRequest, getOrganizationId());
    }

    @DeleteMapping(value = "/apps/{appId}")
    public App delete(@PathVariable String appId) throws Throwable {
        checkAccess(accessService, TargetType.APP, appId, Permission.DELETE);
        return appService.delete(appId, getOrganizationId());
    }

    @GetMapping(value = "/apps/{appId}/access-key")
    public AccessKeyResponse getAccessKey(@PathVariable String appId) throws Throwable {
        checkAccess(accessService, TargetType.APP, appId, Permission.MANAGE_ACCESS_KEY);
        return appService.getAccessKey(appId, getOrganizationId());
    }

    @PutMapping(value = "/apps/{appId}/access-key")
    public AccessKeyResponse resetAccessKey(@PathVariable String appId) throws Throwable {
        checkAccess(accessService, TargetType.APP, appId, Permission.MANAGE_ACCESS_KEY);
        return appService.resetAccessKey(appId, getOrganizationId());
    }

    @GetMapping(value = "/apps/search")
    public List<App> search(@RequestParam String query, Pageable pageable) {
        return appService.search(query, getOrganizationId(), pageable);
    }
}
