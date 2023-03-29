package one.superstack.controllable.executor;

import one.superstack.controllable.auth.app.AuthenticatedApp;
import one.superstack.controllable.pojo.BulkAugmentedPropertyExecutionRequest;
import one.superstack.controllable.response.BulkPropertyExecutionResponse;


public interface PropertyExecutor {

    BulkPropertyExecutionResponse Execute(BulkAugmentedPropertyExecutionRequest request, AuthenticatedApp app);
}
