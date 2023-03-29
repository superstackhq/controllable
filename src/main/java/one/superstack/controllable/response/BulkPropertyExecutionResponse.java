package one.superstack.controllable.response;

import java.io.Serializable;
import java.util.List;

public class BulkPropertyExecutionResponse implements Serializable {

    private List<PropertyExecutionResponse> responses;

    public BulkPropertyExecutionResponse() {

    }

    public BulkPropertyExecutionResponse(List<PropertyExecutionResponse> responses) {
        this.responses = responses;
    }

    public List<PropertyExecutionResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<PropertyExecutionResponse> responses) {
        this.responses = responses;
    }
}
