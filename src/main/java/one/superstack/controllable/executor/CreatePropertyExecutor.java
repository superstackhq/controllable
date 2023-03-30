package one.superstack.controllable.executor;

import one.superstack.controllable.auth.app.AuthenticatedApp;
import one.superstack.controllable.model.PropertyValue;
import one.superstack.controllable.pojo.*;
import one.superstack.controllable.request.PropertyValueCreationRequest;
import one.superstack.controllable.response.BulkPropertyExecutionResponse;
import one.superstack.controllable.response.PropertyExecutionResponse;
import one.superstack.controllable.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreatePropertyExecutor implements PropertyExecutor {

    private final PropertyValueService propertyValueService;

    @Autowired
    public CreatePropertyExecutor(PropertyValueService propertyValueService) {
        this.propertyValueService = propertyValueService;
    }

    @Override
    public BulkPropertyExecutionResponse execute(AugmentedBulkPropertyExecutionRequest request, AuthenticatedApp app) {
        // Construct the requests to create property values
        List<PropertyValueCreationExecutionRequest> propertyValueCreationExecutionRequests = new ArrayList<>();

        for (AugmentedPropertyExecutionRequest augmentedPropertyExecutionRequest : request.getRequests()) {
            PropertyValueCreationRequest value = new PropertyValueCreationRequest();

            PropertyExecutionValue propertyExecutionValue = augmentedPropertyExecutionRequest.getValue();

            value.setSegment(propertyExecutionValue.getSegment());
            value.setValue(propertyExecutionValue.getData());
            value.setRule(propertyExecutionValue.getRule());

            propertyValueCreationExecutionRequests.add(new PropertyValueCreationExecutionRequest(
                    augmentedPropertyExecutionRequest.getProperty().getProperty(), value));
        }

        // Create the property values
        PropertyActor actor = app.toPropertyActor();
        List<PropertyValue> propertyValues = propertyValueService.create(propertyValueCreationExecutionRequests, request.getEnvironment(), actor);

        // Construct the response
        BulkPropertyExecutionResponse response = new BulkPropertyExecutionResponse();
        List<PropertyExecutionResponse> propertyExecutionResponses = new ArrayList<>();

        for (PropertyValue propertyValue : propertyValues) {
            PropertyExecutionResponse propertyExecutionResponse = new PropertyExecutionResponse();
            propertyExecutionResponse.setSuccess(true);
            propertyExecutionResponse.setValue(new PropertyExecutionValue(propertyValue));
            propertyExecutionResponses.add(propertyExecutionResponse);
        }

        response.setResponses(propertyExecutionResponses);
        return response;
    }
}
