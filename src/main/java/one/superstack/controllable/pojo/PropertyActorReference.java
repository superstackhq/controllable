package one.superstack.controllable.pojo;

import one.superstack.controllable.enums.PropertyActorType;

import java.io.Serializable;
import java.util.Objects;

public class PropertyActorReference implements Serializable {

    private PropertyActorType type;

    private String id;

    public PropertyActorReference() {

    }

    public PropertyActorReference(PropertyActorType type, String id) {
        this.type = type;
        this.id = id;
    }

    public PropertyActorType getType() {
        return type;
    }

    public void setType(PropertyActorType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyActorReference that = (PropertyActorReference) o;
        return type == that.type && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }
}
