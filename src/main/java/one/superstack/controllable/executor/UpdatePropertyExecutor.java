package one.superstack.controllable.executor;

import one.superstack.controllable.auth.app.AuthenticatedApp;
import one.superstack.controllable.model.PropertyValue;
import one.superstack.controllable.pojo.AugmentedBulkPropertyExecutionRequest;
import one.superstack.controllable.pojo.AugmentedPropertyExecutionRequest;
import one.superstack.controllable.pojo.PropertyExecutionValue;
import one.superstack.controllable.request.PropertyValueUpdateRequest;
import one.superstack.controllable.response.BulkPropertyExecutionResponse;
import one.superstack.controllable.response.PropertyExecutionResponse;
import one.superstack.controllable.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdatePropertyExecutor implements PropertyExecutor {

    private final PropertyValueService propertyValueLogService;

    @Autowired
    public UpdatePropertyExecutor(PropertyValueService propertyValueLogService) {
        this.propertyValueLogService = propertyValueLogService;
    }

    @Override
    public BulkPropertyExecutionResponse execute(AugmentedBulkPropertyExecutionRequest request, AuthenticatedApp app) {
        List<PropertyExecutionResponse> propertyExecutionResponses = new ArrayList<>();

        for (AugmentedPropertyExecutionRequest augmentedPropertyExecutionRequest : request.getRequests()) {
            try {
                PropertyExecutionValue value = augmentedPropertyExecutionRequest.getValue();

                PropertyValueUpdateRequest propertyValueUpdateRequest = new PropertyValueUpdateRequest();
                propertyValueUpdateRequest.setValue(value.getData());
                propertyValueUpdateRequest.setRule(value.getRule());

                PropertyValue propertyValue = propertyValueLogService.update(
                        augmentedPropertyExecutionRequest.getValue().getId(),
                        augmentedPropertyExecutionRequest.getProperty().getProperty(),
                        request.getEnvironment(),
                        propertyValueUpdateRequest,
                        app.toPropertyActor());
                propertyExecutionResponses.add(new PropertyExecutionResponse(true, new PropertyExecutionValue(propertyValue)));
            } catch (Throwable e) {
                propertyExecutionResponses.add(new PropertyExecutionResponse(false, null));
            }
        }

        BulkPropertyExecutionResponse response = new BulkPropertyExecutionResponse();
        response.setResponses(propertyExecutionResponses);
        return response;
    }
}
