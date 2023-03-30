package one.superstack.controllable.executor;

import one.superstack.controllable.auth.app.AuthenticatedApp;
import one.superstack.controllable.enums.PropertyActorType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.model.PropertyValue;
import one.superstack.controllable.pojo.*;
import one.superstack.controllable.response.BulkPropertyExecutionResponse;
import one.superstack.controllable.response.PropertyExecutionResponse;
import one.superstack.controllable.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DeletePropertyExecutor implements PropertyExecutor {

    private final PropertyValueService propertyValueService;

    @Autowired
    public DeletePropertyExecutor(PropertyValueService propertyValueService) {
        this.propertyValueService = propertyValueService;
    }

    @Override
    public BulkPropertyExecutionResponse execute(AugmentedBulkPropertyExecutionRequest request, AuthenticatedApp app) {
        // Construct the property value references
        Set<PropertyValueReference> propertyValueReferences = new HashSet<>();

        for (AugmentedPropertyExecutionRequest augmentedPropertyExecutionRequest : request.getRequests()) {
            if (null == augmentedPropertyExecutionRequest.getValue()
                    || null == augmentedPropertyExecutionRequest.getValue().getId()
                    || augmentedPropertyExecutionRequest.getValue().getId().isBlank()) {
                throw new ClientException("Property value id is missing");
            }

            String propertyValueId = augmentedPropertyExecutionRequest.getValue().getId();
            String propertyId = augmentedPropertyExecutionRequest.getProperty().getProperty().getId();

            propertyValueReferences.add(new PropertyValueReference(propertyValueId, propertyId, request.getEnvironment().getId()));
        }

        // Delete the property value references
        PropertyActor propertyActor = app.toPropertyActor();
        List<PropertyValue> deletedPropertyValues = propertyValueService.delete(propertyValueReferences, propertyActor);
        Map<PropertyValueReference, PropertyValue> deletedPropertyValueMap = deletedPropertyValues.stream().collect(Collectors.toMap(PropertyValue::toReference, deletedPropertyValue -> deletedPropertyValue, (a, b) -> b));

        // Construct the response
        BulkPropertyExecutionResponse response = new BulkPropertyExecutionResponse();
        List<PropertyExecutionResponse> propertyExecutionResponses = new ArrayList<>();

        for (AugmentedPropertyExecutionRequest augmentedPropertyExecutionRequest : request.getRequests()) {
            String propertyValueId = augmentedPropertyExecutionRequest.getValue().getId();
            String propertyId = augmentedPropertyExecutionRequest.getProperty().getProperty().getId();
            PropertyValue deletedPropertyValue = deletedPropertyValueMap.get(new PropertyValueReference(propertyValueId, propertyId, request.getEnvironment().getId()));

            boolean success = false;
            PropertyExecutionValue value = null;

            if (null != deletedPropertyValue) {
                success = true;
                value = new PropertyExecutionValue(deletedPropertyValue);
            }

            propertyExecutionResponses.add(new PropertyExecutionResponse(success, value));
        }

        response.setResponses(propertyExecutionResponses);
        return response;
    }
}
