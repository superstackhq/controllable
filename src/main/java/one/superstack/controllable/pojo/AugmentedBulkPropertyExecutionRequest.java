package one.superstack.controllable.pojo;

import one.superstack.controllable.enums.ExecutionOperation;
import one.superstack.controllable.model.Environment;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class AugmentedBulkPropertyExecutionRequest implements Serializable {

    private ExecutionOperation operation;

    private Environment environment;

    private List<AugmentedPropertyExecutionRequest> requests;

    private Set<String> propertyIds;

    public AugmentedBulkPropertyExecutionRequest() {

    }

    public AugmentedBulkPropertyExecutionRequest(ExecutionOperation operation, Environment environment, List<AugmentedPropertyExecutionRequest> requests, Set<String> propertyIds) {
        this.operation = operation;
        this.environment = environment;
        this.requests = requests;
        this.propertyIds = propertyIds;
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

    public Set<String> getPropertyIds() {
        return propertyIds;
    }

    public void setPropertyIds(Set<String> propertyIds) {
        this.propertyIds = propertyIds;
    }
}
