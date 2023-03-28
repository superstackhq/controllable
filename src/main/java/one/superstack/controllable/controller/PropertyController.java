package one.superstack.controllable.controller;

import jakarta.validation.Valid;
import one.superstack.controllable.auth.AuthenticatedController;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.model.Property;
import one.superstack.controllable.request.PropertyCreationRequest;
import one.superstack.controllable.request.PropertyFetchRequest;
import one.superstack.controllable.request.PropertyUpdateRequest;
import one.superstack.controllable.service.AccessService;
import one.superstack.controllable.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class PropertyController extends AuthenticatedController {

    private final PropertyService propertyService;

    private final AccessService accessService;

    @Autowired
    public PropertyController(PropertyService propertyService, AccessService accessService) {
        this.propertyService = propertyService;
        this.accessService = accessService;
    }

    @PostMapping(value = "/properties")
    public Property create(@Valid @RequestBody PropertyCreationRequest propertyCreationRequest) {
        checkAccess(accessService, TargetType.PROPERTY, null, Permission.CREATE);
        return propertyService.create(propertyCreationRequest, getActor());
    }

    @GetMapping(value = "/properties")
    public List<Property> listAll(Pageable pageable) {
        return propertyService.listAll(getOrganizationId(), pageable);
    }

    @PostMapping(value = "/properties/fetch")
    public List<Property> list(@Valid @RequestBody PropertyFetchRequest propertyFetchRequest, Pageable pageable) {
        return propertyService.list(propertyFetchRequest, getOrganizationId(), pageable);
    }

    @GetMapping(value = "/properties/{propertyId}")
    public Property get(@PathVariable String propertyId) throws Throwable {
        return propertyService.get(propertyId, getOrganizationId());
    }

    @PutMapping(value = "/properties/{propertyId}")
    public Property update(@PathVariable String propertyId, @Valid @RequestBody PropertyUpdateRequest propertyUpdateRequest) throws Throwable {
        checkAccess(accessService, TargetType.PROPERTY, propertyId, Permission.UPDATE);
        return propertyService.update(propertyId, propertyUpdateRequest, getOrganizationId());
    }

    @DeleteMapping(value = "/properties/{propertyId}")
    public Property delete(@PathVariable String propertyId) throws Throwable {
        checkAccess(accessService, TargetType.PROPERTY, propertyId, Permission.DELETE);
        return propertyService.delete(propertyId, getActor());
    }
}
