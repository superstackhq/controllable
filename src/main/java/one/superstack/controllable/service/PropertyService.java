package one.superstack.controllable.service;

import one.superstack.controllable.auth.actor.AuthenticatedActor;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.Property;
import one.superstack.controllable.pojo.PropertyReference;
import one.superstack.controllable.repository.PropertyRepository;
import one.superstack.controllable.request.*;
import one.superstack.controllable.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    private final MongoTemplate mongoTemplate;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, AccessService accessService, NamespaceService namespaceService, AppAccessGCService appAccessGCService, PropertyValueGCService propertyValueGCService, MongoTemplate mongoTemplate) {
        this.propertyRepository = propertyRepository;
        this.accessService = accessService;
        this.namespaceService = namespaceService;
        this.appAccessGCService = appAccessGCService;
        this.propertyValueGCService = propertyValueGCService;
        this.mongoTemplate = mongoTemplate;
    }

    public Property create(PropertyCreationRequest propertyCreationRequest, AuthenticatedActor creator) {
        if (null != propertyCreationRequest.getNamespace() && propertyCreationRequest.getNamespace().isEmpty()) {
            propertyCreationRequest.setNamespace(null);
        }

        if (null != propertyCreationRequest.getVersion() && propertyCreationRequest.getVersion().isBlank()) {
            propertyCreationRequest.setVersion(null);
        }

        validatePropertyCreationRequest(propertyCreationRequest);

        if (keyVersionExists(propertyCreationRequest.getNamespace(), propertyCreationRequest.getKey(), propertyCreationRequest.getVersion(), creator.getOrganizationId())) {
            throw new ClientException("Property " + propertyCreationRequest.getKey() + " with version " + propertyCreationRequest.getVersion() + " already exists in this namespace");
        }

        if (null != propertyCreationRequest.getNamespace()) {
            namespaceService.register(propertyCreationRequest.getNamespace(), creator.getOrganizationId());
        }

        if (null != propertyCreationRequest.getSegmentTreeStructure()) {
            if (null == propertyCreationRequest.getSegmentTreeStructure().getLevels() || propertyCreationRequest.getSegmentTreeStructure().getLevels().isEmpty()) {
                propertyCreationRequest.setSegmentTreeStructure(null);
            }
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

        accessService.add(new AccessRequest(TargetType.PROPERTY, property.getId(), creator.getType(), creator.getId(), AccessService.ANY_ENVIRONMENT, Set.of(Permission.ALL)), creator);
        accessService.add(new AccessRequest(TargetType.PROPERTY, property.getId(), creator.getType(), creator.getId(), null, Set.of(Permission.ALL)), creator);

        return property;
    }

    public List<Property> listAll(String organizationId, Pageable pageable) {
        return propertyRepository.findByOrganizationId(organizationId, pageable);
    }

    public List<Property> list(PropertyFetchRequest propertyFetchRequest, String organizationId, Pageable pageable) {
        return propertyRepository.findByNamespaceAndOrganizationId(propertyFetchRequest.getNamespace(), organizationId, pageable);
    }

    public Property get(String propertyId) throws Throwable {
        return propertyRepository.findById(propertyId).orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Property not found"));
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

    public List<Property> get(Set<PropertyReference> propertyReferences, String organizationId) {
        List<Criteria> propertyReferenceCriteriaList = new ArrayList<>();

        for (PropertyReference propertyReference : propertyReferences) {
            propertyReferenceCriteriaList.add(Criteria
                    .where("namespace").is(propertyReference.getNamespace())
                    .and("key").is(propertyReference.getKey())
                    .and("version").is(propertyReference.getVersion()));
        }

        Criteria criteria = Criteria.where("organizationId").is(organizationId).orOperator(propertyReferenceCriteriaList);
        return mongoTemplate.find(Query.query(criteria), Property.class);
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

    private void validatePropertyCreationRequest(PropertyCreationRequest propertyCreationRequest) {
        if (null != propertyCreationRequest.getNamespace() && !propertyCreationRequest.getNamespace().isEmpty()) {
            for (String namespaceComponent : propertyCreationRequest.getNamespace()) {
                if (!StringUtil.isAlphaNumeric(namespaceComponent)) {
                    throw new ClientException("Namespace contains non alpha numeric characters");
                }
            }
        }

        if (!StringUtil.isAlphaNumeric(propertyCreationRequest.getKey())) {
            throw new ClientException("Property key contains non alpha numeric characters");
        }

        if (null != propertyCreationRequest.getVersion() && !propertyCreationRequest.getVersion().isBlank()) {
            if (!StringUtil.isAlphaNumeric(propertyCreationRequest.getVersion())) {
                throw new ClientException("Property version contains non alpha numeric characters");
            }
        }
    }
}
