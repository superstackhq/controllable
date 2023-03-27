package one.superstack.controllable.pojo;

import one.superstack.controllable.enums.TargetType;

import java.io.Serializable;
import java.util.Objects;

public class TargetReference implements Serializable {

    private TargetType type;

    private String id;

    public TargetReference() {

    }

    public TargetReference(TargetType type, String id) {
        this.type = type;
        this.id = id;
    }

    public TargetType getType() {
        return type;
    }

    public void setType(TargetType type) {
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
        TargetReference that = (TargetReference) o;
        return type == that.type && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }
}
