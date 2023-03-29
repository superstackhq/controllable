package one.superstack.controllable.pojo;

import java.io.Serializable;
import java.util.Map;

public class AugmentedPropertyExecutionRequest implements Serializable {

    private AugmentedProperty property;

    private PropertyExecutionValue value;

    private Map<String, Object> params;

    public AugmentedPropertyExecutionRequest() {

    }

    public AugmentedPropertyExecutionRequest(AugmentedProperty property, PropertyExecutionValue value, Map<String, Object> params) {
        this.property = property;
        this.value = value;
        this.params = params;
    }

    public AugmentedProperty getProperty() {
        return property;
    }

    public void setProperty(AugmentedProperty property) {
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
