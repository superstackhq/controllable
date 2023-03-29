package one.superstack.controllable.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import one.superstack.controllable.enums.ExecutionOperation;

import java.io.Serializable;
import java.util.List;

public class BulkPropertyExecutionRequest implements Serializable {

    @NotNull
    private ExecutionOperation operation;

    @NotBlank
    private String environment;

    @NotEmpty
    private List<PropertyExecutionRequest> requests;

    public BulkPropertyExecutionRequest() {

    }

    public BulkPropertyExecutionRequest(ExecutionOperation operation, String environment, List<PropertyExecutionRequest> requests) {
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

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public List<PropertyExecutionRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<PropertyExecutionRequest> requests) {
        this.requests = requests;
    }
}
