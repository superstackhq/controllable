package one.superstack.controllable.executor;

import one.superstack.controllable.auth.app.AuthenticatedApp;
import one.superstack.controllable.pojo.AugmentedBulkPropertyExecutionRequest;
import one.superstack.controllable.response.BulkPropertyExecutionResponse;


public interface PropertyExecutor {

    BulkPropertyExecutionResponse execute(AugmentedBulkPropertyExecutionRequest request, AuthenticatedApp app);
}
