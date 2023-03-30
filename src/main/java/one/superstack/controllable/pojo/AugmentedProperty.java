package one.superstack.controllable.pojo;

import one.superstack.controllable.model.Property;

import java.io.Serializable;

public class AugmentedProperty implements Serializable {

    private PropertyReference reference;

    private Property property;

    public AugmentedProperty() {

    }

    public AugmentedProperty(PropertyReference reference, Property property) {
        this.reference = reference;
        this.property = property;
    }

    public PropertyReference getReference() {
        return reference;
    }

    public void setReference(PropertyReference reference) {
        this.reference = reference;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
}
