package one.superstack.controllable.request;

import jakarta.validation.constraints.NotNull;
import one.superstack.controllable.pojo.PropertyExecutionValue;
import one.superstack.controllable.pojo.PropertyReference;

import java.io.Serializable;
import java.util.Map;

public class PropertyExecutionRequest implements Serializable {

    @NotNull
    private PropertyReference property;

    private PropertyExecutionValue value;

    private Map<String, Object> params;

    public PropertyExecutionRequest() {

    }

    public PropertyExecutionRequest(PropertyReference property, PropertyExecutionValue value, Map<String, Object> params) {
        this.property = property;
        this.value = value;
        this.params = params;
    }

    public PropertyReference getProperty() {
        return property;
    }

    public void setProperty(PropertyReference property) {
        this.property = property;
    }

    public PropertyExecutionValue getValue() {
        return value;
    }

    public void setValue(PropertyExecutionValue value) {
        this.value = value;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
