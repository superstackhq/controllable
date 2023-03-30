package one.superstack.controllable.executor;

import one.superstack.controllable.auth.app.AuthenticatedApp;
import one.superstack.controllable.pojo.AugmentedBulkPropertyExecutionRequest;
import one.superstack.controllable.response.BulkPropertyExecutionResponse;
import org.springframework.stereotype.Component;

@Component
public class CreatePropertyExecutor implements PropertyExecutor {

    @Override
    public BulkPropertyExecutionResponse execute(AugmentedBulkPropertyExecutionRequest request, AuthenticatedApp app) {
        // TODO
        return null;
    }
}
