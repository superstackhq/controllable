package one.superstack.controllable.pojo;

import one.superstack.controllable.model.Property;
import one.superstack.controllable.request.PropertyValueCreationRequest;

import java.io.Serializable;

public class PropertyValueCreationExecutionRequest implements Serializable {

    private Property property;

    private PropertyValueCreationRequest value;

    public PropertyValueCreationExecutionRequest() {

    }

    public PropertyValueCreationExecutionRequest(Property property, PropertyValueCreationRequest value) {
        this.property = property;
        this.value = value;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public PropertyValueCreationRequest getValue() {
        return value;
    }

    public void setValue(PropertyValueCreationRequest value) {
        this.value = value;
    }
}
