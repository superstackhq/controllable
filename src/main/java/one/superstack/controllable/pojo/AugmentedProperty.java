package one.superstack.controllable.pojo;

import one.superstack.controllable.model.Property;

import java.io.Serializable;

public class AugmentedProperty implements Serializable {

    private PropertyReference reference;

    private String referenceString;

    private Property property;


    public AugmentedProperty(PropertyReference reference, String referenceString, Property property) {
        this.reference = reference;
        this.referenceString = referenceString;
        this.property = property;
    }
}
