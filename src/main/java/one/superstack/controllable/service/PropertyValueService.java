package one.superstack.controllable.service;

import jakarta.validation.Valid;
import one.superstack.controllable.auth.actor.AuthenticatedActor;
import one.superstack.controllable.enums.ChangeType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.Property;
import one.superstack.controllable.model.PropertyValue;
import one.superstack.controllable.model.PropertyValueLog;
import one.superstack.controllable.repository.PropertyValueRepository;
import one.superstack.controllable.request.PropertyValueCreationRequest;
import one.superstack.controllable.request.PropertyValueDeletionRequest;
import one.superstack.controllable.request.PropertyValueFetchRequest;
import one.superstack.controllable.request.PropertyValueUpdateRequest;
import one.superstack.controllable.util.ActorUtil;
import one.superstack.controllable.util.DataTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

@Service
public class PropertyValueService {

    private final PropertyValueRepository propertyValueRepository;

    private final PropertyService propertyService;

    private final PropertyValueLogService propertyValueLogService;

    private final EnvironmentService environmentService;

    @Autowired
    public PropertyValueService(PropertyValueRepository propertyValueRepository, PropertyService propertyService, PropertyValueLogService propertyValueLogService, EnvironmentService environmentService) {
        this.propertyValueRepository = propertyValueRepository;
        this.propertyService = propertyService;
        this.propertyValueLogService = propertyValueLogService;
        this.environmentService = environmentService;
    }

    public PropertyValue create(String propertyId, String environmentId, PropertyValueCreationRequest propertyValueCreationRequest, AuthenticatedActor creator) throws Throwable {
        Property property = propertyService.get(propertyId, creator.getOrganizationId());

        if (!environmentService.exists(environmentId, creator.getOrganizationId())) {
            throw new NotFoundException("Environment not found");
        }

        if (!DataTypeValidator.validate(property.getDataType(), propertyValueCreationRequest.getValue())) {
            throw new ClientException("Invalid property value data type");
        }

        if (null != property.getConstraints()) {
            property.getConstraints().validate(propertyValueCreationRequest.getValue(), property.getDataType());
        }

        if (null != property.getSegmentTreeStructure()) {
            propertyValueCreationRequest.setSegment(property.getSegmentTreeStructure().validateSegment(propertyValueCreationRequest.getSegment()));
        }

        PropertyValue propertyValue = new PropertyValue(propertyId,
                environmentId,
                propertyValueCreationRequest.getValue(),
                propertyValueCreationRequest.getSegment(),
                propertyValueCreationRequest.getRule(),
                creator.getOrganizationId(),
                ActorUtil.convert(creator.getType()),
                creator.getId());

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

    public PropertyValue update(String propertyValueId, String propertyId, String environmentId, PropertyValueUpdateRequest propertyValueUpdateRequest, AuthenticatedActor actor) throws Throwable {
        PropertyValue propertyValue = get(propertyValueId, propertyId, environmentId, actor.getOrganizationId());
        Property property = propertyService.get(propertyId, actor.getOrganizationId());

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
                ActorUtil.convert(actor.getType()),
                actor.getId()));

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
}
