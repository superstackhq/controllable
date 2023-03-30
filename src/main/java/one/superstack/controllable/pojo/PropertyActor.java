package one.superstack.controllable.pojo;

import one.superstack.controllable.auth.actor.AuthenticatedActor;
import one.superstack.controllable.enums.PropertyActorType;
import one.superstack.controllable.util.ActorUtil;

import java.io.Serializable;

public class PropertyActor implements Serializable {

    private PropertyActorType type;

    private String referenceId;

    private Object data;

    public PropertyActor() {

    }

    public PropertyActor(PropertyActorType type, String referenceId, Object data) {
        this.type = type;
        this.referenceId = referenceId;
        this.data = data;
    }

    public static PropertyActor fromAuthenticatedActor(AuthenticatedActor actor) {
        return new PropertyActor(ActorUtil.convert(actor.getType()), actor.getId(), null);
    }

    public PropertyActorType getType() {
        return type;
    }

    public void setType(PropertyActorType type) {
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
