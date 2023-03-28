package one.superstack.controllable.service;

import one.superstack.controllable.auth.AuthenticatedActor;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.Property;
import one.superstack.controllable.repository.PropertyRepository;
import one.superstack.controllable.request.AccessRequest;
import one.superstack.controllable.request.PropertyCreationRequest;
import one.superstack.controllable.request.PropertyFetchRequest;
import one.superstack.controllable.request.PropertyUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    private final AccessService accessService;

    private final NamespaceService namespaceService;

    private final AppAccessGCService appAccessGCService;

    private final PropertyValueGCService propertyValueGCService;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, AccessService accessService, NamespaceService namespaceService, AppAccessGCService appAccessGCService, PropertyValueGCService propertyValueGCService) {
        this.propertyRepository = propertyRepository;
        this.accessService = accessService;
        this.namespaceService = namespaceService;
        this.appAccessGCService = appAccessGCService;
        this.propertyValueGCService = propertyValueGCService;
    }

    public Property create(PropertyCreationRequest propertyCreationRequest, AuthenticatedActor creator) {
        if (null != propertyCreationRequest.getNamespace() && propertyCreationRequest.getNamespace().isEmpty()) {
            propertyCreationRequest.setNamespace(null);
        }

        if (null != propertyCreationRequest.getNamespace()) {
            namespaceService.register(propertyCreationRequest.getNamespace(), creator.getOrganizationId());
        }

        if (keyVersionExists(propertyCreationRequest.getNamespace(), propertyCreationRequest.getKey(), propertyCreationRequest.getVersion(), creator.getOrganizationId())) {
            throw new ClientException("Property " + propertyCreationRequest.getKey() + " with version " + propertyCreationRequest.getVersion() + " already exists in this namespace");
        }

        Property property = new Property(propertyCreationRequest.getNamespace(),
                propertyCreationRequest.getKey(),
                propertyCreationRequest.getVersion(),
                propertyCreationRequest.getDescription(),
                propertyCreationRequest.getDataType(),
                propertyCreationRequest.getSegmentTreeStructure(),
                propertyCreationRequest.getConstraints(),
                creator.getOrganizationId(),
                creator.getType(),
                creator.getId());

        property = propertyRepository.save(property);
        accessService.add(new AccessRequest(TargetType.PROPERTY, property.getId(), creator.getType(), creator.getId(), AccessService.ALL_ENVIRONMENT, Set.of(Permission.ALL)), creator);
        return property;
    }

    public List<Property> listAll(String organizationId, Pageable pageable) {
        return propertyRepository.findByOrganizationId(organizationId, pageable);
    }

    public List<Property> list(PropertyFetchRequest propertyFetchRequest, String organizationId, Pageable pageable) {
        return propertyRepository.findByNamespaceAndOrganizationId(propertyFetchRequest.getNamespace(), organizationId, pageable);
    }

    public Property get(String propertyId, String organizationId) throws Throwable {
        return propertyRepository.findByIdAndOrganizationId(propertyId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Property not found"));
    }

    @Async
    public CompletableFuture<List<Property>> asyncGet(List<String> propertyIds) {
        return CompletableFuture.completedFuture(get(propertyIds));
    }

    public List<Property> get(List<String> propertyIds) {
        return propertyRepository.findByIdIn(propertyIds);
    }

    public Property update(String propertyId, PropertyUpdateRequest propertyUpdateRequest, String organizationId) throws Throwable {
        Property property = get(propertyId, organizationId);
        property.setDescription(propertyUpdateRequest.getDescription());
        property.setModifiedOn(new Date());
        return propertyRepository.save(property);
    }

    public Property delete(String propertyId, AuthenticatedActor actor) throws Throwable {
        Property property = get(propertyId, actor.getOrganizationId());
        propertyRepository.delete(property);
        accessService.deleteAllForTarget(TargetType.PROPERTY, propertyId);
        appAccessGCService.deleteAllForTarget(TargetType.PROPERTY, propertyId);
        propertyValueGCService.deleteAllForProperty(propertyId, actor);
        return property;
    }

    public Boolean exists(String propertyId, String organizationId) {
        return propertyRepository.existsByIdAndOrganizationId(propertyId, organizationId);
    }

    private Boolean keyVersionExists(List<String> namespace, String key, String version, String organizationId) {
        return propertyRepository.existsByNamespaceAndKeyAndVersionAndOrganizationId(namespace, key, version, organizationId);
    }
}
