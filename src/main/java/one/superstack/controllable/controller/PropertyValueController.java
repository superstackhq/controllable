package one.superstack.controllable.controller;

import jakarta.validation.Valid;
import one.superstack.controllable.auth.actor.AuthenticatedController;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.model.PropertyValue;
import one.superstack.controllable.request.PropertyValueCreationRequest;
import one.superstack.controllable.request.PropertyValueDeletionRequest;
import one.superstack.controllable.request.PropertyValueFetchRequest;
import one.superstack.controllable.request.PropertyValueUpdateRequest;
import one.superstack.controllable.service.AffordanceAccessService;
import one.superstack.controllable.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/environments/{environmentId}/properties/{propertyId}")
public class PropertyValueController extends AuthenticatedController {

    private final PropertyValueService propertyValueService;

    private final AffordanceAccessService accessService;

    @Autowired
    public PropertyValueController(PropertyValueService propertyValueService, AffordanceAccessService accessService) {
        this.propertyValueService = propertyValueService;
        this.accessService = accessService;
    }

    @PostMapping(value = "/values")
    public PropertyValue create(@PathVariable String propertyId, @PathVariable String environmentId, @Valid @RequestBody PropertyValueCreationRequest propertyValueCreationRequest) throws Throwable {
        checkAccess(accessService, TargetType.PROPERTY, propertyId, environmentId, Permission.CREATE_PROPERTY_VALUE);
        return propertyValueService.create(propertyId, environmentId, propertyValueCreationRequest, getActor());
    }

    @GetMapping(value = "/values")
    public List<PropertyValue> listAll(@PathVariable String propertyId, @PathVariable String environmentId, Pageable pageable) {
        checkAccess(accessService, TargetType.PROPERTY, propertyId, environmentId, Permission.READ_PROPERTY_VALUE);
        return propertyValueService.listAll(propertyId, environmentId, getOrganizationId(), pageable);
    }

    @PostMapping(value = "/values/fetch")
    public List<PropertyValue> list(@PathVariable String propertyId, @PathVariable String environmentId, @Valid @RequestBody PropertyValueFetchRequest propertyValueFetchRequest, Pageable pageable) {
        checkAccess(accessService, TargetType.PROPERTY, propertyId, environmentId, Permission.READ_PROPERTY_VALUE);
        return propertyValueService.list(propertyId, environmentId, propertyValueFetchRequest, getOrganizationId(), pageable);
    }

    @GetMapping(value = "/values/{valueId}")
    public PropertyValue get(@PathVariable String propertyId, @PathVariable String environmentId, @PathVariable String valueId) throws Throwable {
        checkAccess(accessService, TargetType.PROPERTY, propertyId, environmentId, Permission.READ_PROPERTY_VALUE);
        return propertyValueService.get(valueId, propertyId, environmentId, getOrganizationId());
    }

    @PutMapping(value = "/values/{valueId}")
    public PropertyValue update(@PathVariable String propertyId, @PathVariable String environmentId, @PathVariable String valueId, @Valid @RequestBody PropertyValueUpdateRequest propertyValueUpdateRequest) throws Throwable {
        checkAccess(accessService, TargetType.PROPERTY, propertyId, environmentId, Permission.UPDATE_PROPERTY_VALUE);
        return propertyValueService.update(valueId, propertyId, environmentId, propertyValueUpdateRequest, getActor());
    }

    @DeleteMapping(value = "/values/{valueId}")
    public PropertyValue delete(@PathVariable String propertyId, @PathVariable String environmentId, @PathVariable String valueId, @Valid @RequestBody PropertyValueDeletionRequest propertyValueDeletionRequest) throws Throwable {
        checkAccess(accessService, TargetType.PROPERTY, propertyId, environmentId, Permission.DELETE_PROPERTY_VALUE);
        return propertyValueService.delete(valueId, propertyId, environmentId, propertyValueDeletionRequest, getActor());
    }
}
