package one.superstack.controllable.executor;

import one.superstack.controllable.auth.app.AuthenticatedApp;
import one.superstack.controllable.pojo.BulkAugmentedPropertyExecutionRequest;
import one.superstack.controllable.response.BulkPropertyExecutionResponse;
import org.springframework.stereotype.Component;

@Component
public class ReadPropertyExecutor implements PropertyExecutor {

    @Override
    public BulkPropertyExecutionResponse Execute(BulkAugmentedPropertyExecutionRequest request, AuthenticatedApp app) {
        // TODO
        return null;
    }
}
