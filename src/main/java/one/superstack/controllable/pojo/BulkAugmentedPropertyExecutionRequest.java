package one.superstack.controllable.pojo;

import one.superstack.controllable.enums.ExecutionOperation;
import one.superstack.controllable.model.Environment;

import java.io.Serializable;
import java.util.List;

public class BulkAugmentedPropertyExecutionRequest implements Serializable {

    private ExecutionOperation operation;

    private Environment environment;

    private List<AugmentedPropertyExecutionRequest> requests;

    public BulkAugmentedPropertyExecutionRequest() {

    }

    public BulkAugmentedPropertyExecutionRequest(ExecutionOperation operation, Environment environment, List<AugmentedPropertyExecutionRequest> requests) {
        this.operation = operation;
        this.environment = environment;
        this.requests = requests;
    }

    public ExecutionOperation getOperation() {
        return operation;
    }

    public void setOperation(ExecutionOperation operation) {
        this.operation = operation;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public List<AugmentedPropertyExecutionRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<AugmentedPropertyExecutionRequest> requests) {
        this.requests = requests;
    }
}
