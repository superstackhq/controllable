package one.superstack.controllable.controller;

import one.superstack.controllable.auth.actor.AuthenticatedController;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.model.PropertyValueLog;
import one.superstack.controllable.response.PropertyValueLogResponse;
import one.superstack.controllable.service.AccessService;
import one.superstack.controllable.service.PropertyValueLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/api/v1/environments/{environmentId}/properties/{propertyId}/values/{valueId}")
public class PropertyValueLogController extends AuthenticatedController {

    private final PropertyValueLogService propertyValueLogService;

    private final AccessService accessService;

    @Autowired
    public PropertyValueLogController(PropertyValueLogService propertyValueLogService, AccessService accessService) {
        this.propertyValueLogService = propertyValueLogService;
        this.accessService = accessService;
    }

    @GetMapping(value = "/logs")
    public List<PropertyValueLogResponse> list(@PathVariable String environmentId, @PathVariable String propertyId, @PathVariable String valueId, Pageable pageable) throws ExecutionException, InterruptedException {
        checkAccess(accessService, TargetType.PROPERTY, propertyId, environmentId, Permission.READ_PROPERTY_VALUE);
        return propertyValueLogService.list(valueId, propertyId, environmentId, getOrganizationId(), pageable);
    }

    @GetMapping(value = "/logs/{logId}")
    public PropertyValueLog get(@PathVariable String environmentId, @PathVariable String propertyId, @PathVariable String valueId, @PathVariable String logId) throws Throwable {
        checkAccess(accessService, TargetType.PROPERTY, propertyId, environmentId, Permission.READ_PROPERTY_VALUE);
        return propertyValueLogService.get(logId, valueId, propertyId, environmentId, getOrganizationId());
    }
}
