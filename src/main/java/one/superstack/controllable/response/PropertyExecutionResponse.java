package one.superstack.controllable.response;

import one.superstack.controllable.pojo.PropertyExecutionValue;

import java.io.Serializable;

public class PropertyExecutionResponse implements Serializable {

    private Boolean success;

    private PropertyExecutionValue value;

    public PropertyExecutionResponse() {

    }

    public PropertyExecutionResponse(Boolean success, PropertyExecutionValue value) {
        this.success = success;
        this.value = value;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public PropertyExecutionValue getValue() {
        return value;
    }

    public void setValue(PropertyExecutionValue value) {
        this.value = value;
    }
}
