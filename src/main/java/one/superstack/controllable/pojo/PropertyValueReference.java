package one.superstack.controllable.pojo;

import java.io.Serializable;
import java.util.Objects;

public class PropertyValueReference implements Serializable {

    private String id;

    private String propertyId;

    private String environmentId;

    public PropertyValueReference() {

    }

    public PropertyValueReference(String id, String propertyId, String environmentId) {
        this.id = id;
        this.propertyId = propertyId;
        this.environmentId = environmentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(String environmentId) {
        this.environmentId = environmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyValueReference that = (PropertyValueReference) o;
        return Objects.equals(id, that.id) && Objects.equals(propertyId, that.propertyId) && Objects.equals(environmentId, that.environmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, propertyId, environmentId);
    }
}
