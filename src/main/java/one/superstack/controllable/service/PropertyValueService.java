package one.superstack.controllable.service;

import jakarta.validation.Valid;
import one.superstack.controllable.auth.actor.AuthenticatedActor;
import one.superstack.controllable.enums.ChangeType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.Environment;
import one.superstack.controllable.model.Property;
import one.superstack.controllable.model.PropertyValue;
import one.superstack.controllable.model.PropertyValueLog;
import one.superstack.controllable.pojo.PropertyActor;
import one.superstack.controllable.pojo.PropertyValueCreationExecutionRequest;
import one.superstack.controllable.pojo.PropertyValueReference;
import one.superstack.controllable.repository.PropertyValueRepository;
import one.superstack.controllable.request.PropertyValueCreationRequest;
import one.superstack.controllable.request.PropertyValueDeletionRequest;
import one.superstack.controllable.request.PropertyValueFetchRequest;
import one.superstack.controllable.request.PropertyValueUpdateRequest;
import one.superstack.controllable.util.ActorUtil;
import one.superstack.controllable.util.DataTypeValidator;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class PropertyValueService {

    private final PropertyValueRepository propertyValueRepository;

    private final PropertyService propertyService;

    private final PropertyValueLogService propertyValueLogService;

    private final EnvironmentService environmentService;

    private final MongoTemplate mongoTemplate;

    public static final String DEFAULT_SEGMENT = "DEFAULT";

    @Autowired
    public PropertyValueService(PropertyValueRepository propertyValueRepository, PropertyService propertyService, PropertyValueLogService propertyValueLogService, EnvironmentService environmentService, MongoTemplate mongoTemplate) {
        this.propertyValueRepository = propertyValueRepository;
        this.propertyService = propertyService;
        this.propertyValueLogService = propertyValueLogService;
        this.environmentService = environmentService;
        this.mongoTemplate = mongoTemplate;
    }

    public List<PropertyValue> create(List<PropertyValueCreationExecutionRequest> propertyValueCreationExecutionRequests, Environment environment, PropertyActor actor) {
        List<PropertyValue> propertyValues = new ArrayList<>();

        // Create the property values
        for (PropertyValueCreationExecutionRequest propertyValueCreationExecutionRequest : propertyValueCreationExecutionRequests) {
            Property property = propertyValueCreationExecutionRequest.getProperty();
            PropertyValue propertyValue = validateAndCreateInstance(property, propertyValueCreationExecutionRequest.getValue(), environment.getId(), actor);
            propertyValues.add(propertyValue);
        }

        propertyValues = propertyValueRepository.saveAll(propertyValues);

        // Log property value creation
        List<PropertyValueLog> logs = new ArrayList<>();

        for (PropertyValue propertyValue : propertyValues) {
            logs.add(new PropertyValueLog(ChangeType.CREATE,
                    null,
                    propertyValue.getId(),
                    propertyValue.getPropertyId(),
                    propertyValue.getEnvironmentId(),
                    propertyValue.getSegment(),
                    propertyValue.getRule(),
                    propertyValue.getValue(),
                    propertyValue.getOrganizationId(),
                    propertyValue.getCreatorType(),
                    propertyValue.getCreatorId()));
        }

        propertyValueLogService.asyncLog(logs);

        return propertyValues;
    }

    public PropertyValue create(String propertyId, String environmentId, PropertyValueCreationRequest propertyValueCreationRequest, AuthenticatedActor creator) throws Throwable {
        Property property = propertyService.get(propertyId, creator.getOrganizationId());

        if (!environmentService.exists(environmentId, creator.getOrganizationId())) {
            throw new NotFoundException("Environment not found");
        }

        PropertyValue propertyValue = validateAndCreateInstance(property, propertyValueCreationRequest, environmentId, PropertyActor.fromAuthenticatedActor(creator));
        propertyValue = propertyValueRepository.save(propertyValue);

        propertyValueLogService.asyncLog(new PropertyValueLog(ChangeType.CREATE,
                propertyValueCreationRequest.getChangeMessage(),
                propertyValue.getId(),
                propertyValue.getPropertyId(),
                propertyValue.getEnvironmentId(),
                propertyValue.getSegment(),
                propertyValue.getRule(),
                propertyValue.getValue(),
                propertyValue.getOrganizationId(),
                propertyValue.getCreatorType(),
                propertyValue.getCreatorId()));

        return propertyValue;
    }

    private PropertyValue validateAndCreateInstance(Property property, PropertyValueCreationRequest propertyValueCreationRequest, String environmentId, PropertyActor creator) {
        if (!DataTypeValidator.validate(property.getDataType(), propertyValueCreationRequest.getValue())) {
            throw new ClientException("Invalid property value data type");
        }

        if (null != property.getConstraints()) {
            property.getConstraints().validate(propertyValueCreationRequest.getValue(), property.getDataType());
        }

        if (null != property.getSegmentTreeStructure()) {
            propertyValueCreationRequest.setSegment(property.getSegmentTreeStructure().validateSegment(propertyValueCreationRequest.getSegment()));
        }

        if (null != propertyValueCreationRequest.getSegment()) {
            if (null == propertyValueCreationRequest.getSegment().getPath() || propertyValueCreationRequest.getSegment().getPath().isEmpty()) {
                propertyValueCreationRequest.setSegment(null);
            }
        }

        return new PropertyValue(property.getId(),
                environmentId,
                propertyValueCreationRequest.getValue(),
                propertyValueCreationRequest.getSegment(),
                propertyValueCreationRequest.getRule(),
                property.getOrganizationId(),
                creator.getType(),
                creator.getReferenceId());
    }

    public List<PropertyValue> listAll(String propertyId, String environmentId, String organizationId, Pageable pageable) {
        return propertyValueRepository.findByPropertyIdAndEnvironmentIdAndOrganizationId(propertyId, environmentId, organizationId, pageable);
    }

    public List<PropertyValue> list(String propertyId, String environmentId, PropertyValueFetchRequest propertyValueFetchRequest, String organizationId, Pageable pageable) {
        return propertyValueRepository.findByPropertyIdAndEnvironmentIdAndSegmentAndOrganizationId(propertyId, environmentId, propertyValueFetchRequest.getSegment(), organizationId, pageable);
    }

    public PropertyValue get(String propertyValueId, String propertyId, String environmentId, String organizationId) throws Throwable {
        return propertyValueRepository.findByIdAndPropertyIdAndEnvironmentIdAndOrganizationId(propertyValueId, propertyId, environmentId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Property value not found"));
    }

    public List<PropertyValue> get(Set<PropertyValueReference> propertyValueReferences) {
        Criteria criteria = new Criteria();

        List<Criteria> propertyValueReferenceCriteriaList = new ArrayList<>();

        for (PropertyValueReference propertyValueReference : propertyValueReferences) {
            propertyValueReferenceCriteriaList.add(Criteria
                    .where("_id").is(new ObjectId(propertyValueReference.getId()))
                    .and("propertyId").is(propertyValueReference.getPropertyId())
                    .and("environmentId").is(propertyValueReference.getEnvironmentId()));
        }

        criteria.orOperator(propertyValueReferenceCriteriaList);
        return get(Query.query(criteria));
    }

    public List<PropertyValue> get(Query query) {
        return mongoTemplate.find(query, PropertyValue.class);
    }

    public PropertyValue update(String propertyValueId, Property property, Environment environment, PropertyValueUpdateRequest propertyValueUpdateRequest, PropertyActor propertyActor) throws Throwable {
        PropertyValue propertyValue = get(propertyValueId, property.getId(), environment.getId(), environment.getOrganizationId());
        return update(property, propertyValue, propertyValueUpdateRequest, propertyActor);
    }

    public PropertyValue update(String propertyValueId, String propertyId, String environmentId, PropertyValueUpdateRequest propertyValueUpdateRequest, AuthenticatedActor actor) throws Throwable {
        PropertyValue propertyValue = get(propertyValueId, propertyId, environmentId, actor.getOrganizationId());
        Property property = propertyService.get(propertyId, actor.getOrganizationId());
        return update(property, propertyValue, propertyValueUpdateRequest, PropertyActor.fromAuthenticatedActor(actor));
    }

    private PropertyValue update(Property property, PropertyValue propertyValue, PropertyValueUpdateRequest propertyValueUpdateRequest, PropertyActor actor) {
        if (!DataTypeValidator.validate(property.getDataType(), propertyValueUpdateRequest.getValue())) {
            throw new ClientException("Invalid property value data type");
        }

        property.getConstraints().validate(propertyValueUpdateRequest.getValue(), property.getDataType());

        propertyValue.setValue(propertyValueUpdateRequest.getValue());
        propertyValue.setRule(propertyValueUpdateRequest.getRule());
        propertyValue.setModifiedOn(new Date());

        propertyValue = propertyValueRepository.save(propertyValue);

        propertyValueLogService.asyncLog(new PropertyValueLog(ChangeType.UPDATE,
                propertyValueUpdateRequest.getChangeMessage(),
                propertyValue.getId(),
                propertyValue.getPropertyId(),
                propertyValue.getEnvironmentId(),
                propertyValue.getSegment(),
                propertyValue.getRule(),
                propertyValue.getValue(),
                propertyValue.getOrganizationId(),
                actor.getType(),
                actor.getReferenceId()));

        return propertyValue;
    }

    public PropertyValue delete(String propertyValueId, String propertyId, String environmentId, @Valid @RequestBody PropertyValueDeletionRequest propertyValueDeletionRequest, AuthenticatedActor actor) throws Throwable {
        PropertyValue propertyValue = get(propertyValueId, propertyId, environmentId, actor.getOrganizationId());
        propertyValueRepository.delete(propertyValue);

        propertyValueLogService.asyncLog(new PropertyValueLog(ChangeType.DELETE,
                propertyValueDeletionRequest.getChangeMessage(),
                propertyValue.getId(),
                propertyValue.getPropertyId(),
                propertyValue.getEnvironmentId(),
                propertyValue.getSegment(),
                propertyValue.getRule(),
                propertyValue.getValue(),
                propertyValue.getOrganizationId(),
                ActorUtil.convert(actor.getType()),
                actor.getId()));

        return propertyValue;
    }

    public List<PropertyValue> delete(Set<PropertyValueReference> propertyValueReferences, PropertyActor actor) {
        return delete(get(propertyValueReferences), actor);
    }

    public List<PropertyValue> delete(List<PropertyValue> values, PropertyActor actor) {
        propertyValueRepository.deleteAll(values);
        propertyValueLogService.logDeletes(values, actor);
        return values;
    }
}
