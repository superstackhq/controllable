package one.superstack.controllable.service;

import one.superstack.controllable.auth.app.AuthenticatedApp;
import one.superstack.controllable.enums.ExecutionOperation;
import one.superstack.controllable.executor.*;
import one.superstack.controllable.request.BulkPropertyExecutionRequest;
import one.superstack.controllable.response.BulkPropertyExecutionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PropertyExecutionService {

    private final Map<ExecutionOperation, PropertyExecutor> propertyExecutorMap;

    @Autowired
    public PropertyExecutionService(CreatePropertyExecutor createPropertyExecutor,
                                    ReadPropertyExecutor readPropertyExecutor,
                                    UpdatePropertyExecutor updatePropertyExecutor,
                                    DeletePropertyExecutor deletePropertyExecutor) {
        this.propertyExecutorMap = Map.of(
                ExecutionOperation.CREATE_PROPERTY_VALUE, createPropertyExecutor,
                ExecutionOperation.READ_PROPERTY_VALUE, readPropertyExecutor,
                ExecutionOperation.UPDATE_PROPERTY_VALUE, updatePropertyExecutor,
                ExecutionOperation.DELETE_PROPERTY_VALUE, deletePropertyExecutor
        );
    }

    public BulkPropertyExecutionResponse execute(BulkPropertyExecutionRequest request, AuthenticatedApp app) {
        // TODO
        return null;
    }
}
