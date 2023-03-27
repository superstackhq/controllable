package one.superstack.controllable.pojo;

import one.superstack.controllable.enums.ActorType;

import java.io.Serializable;
import java.util.Objects;

public class ActorReference implements Serializable {

    private ActorType type;

    private String id;

    public ActorReference() {

    }

    public ActorReference(ActorType type, String id) {
        this.type = type;
        this.id = id;
    }

    public ActorType getType() {
        return type;
    }

    public void setType(ActorType type) {
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
        ActorReference that = (ActorReference) o;
        return type == that.type && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }
}
