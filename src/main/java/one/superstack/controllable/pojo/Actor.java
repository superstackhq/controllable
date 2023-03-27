package one.superstack.controllable.pojo;

import one.superstack.controllable.enums.ActorType;

import java.io.Serializable;

public class Actor implements Serializable {

    private ActorType type;

    private String referenceId;

    private Object data;

    public Actor() {

    }

    public Actor(ActorType type, String referenceId, Object data) {
        this.type = type;
        this.referenceId = referenceId;
        this.data = data;
    }

    public ActorType getType() {
        return type;
    }

    public void setType(ActorType type) {
        this.type = type;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
