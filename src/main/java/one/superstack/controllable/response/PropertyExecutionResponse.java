package one.superstack.controllable.response;

import one.superstack.controllable.pojo.PropertyExecutionValue;

import java.io.Serializable;

public class PropertyExecutionResponse implements Serializable {

    private Boolean success;

    private String message;

    private PropertyExecutionValue value;

    public PropertyExecutionResponse() {

    }

    public PropertyExecutionResponse(Boolean success, String message, PropertyExecutionValue value) {
        this.success = success;
        this.message = message;
        this.value = value;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PropertyExecutionValue getValue() {
        return value;
    }

    public void setValue(PropertyExecutionValue value) {
        this.value = value;
    }
}
