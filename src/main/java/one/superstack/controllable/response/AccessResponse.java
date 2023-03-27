package one.superstack.controllable.response;

import one.superstack.controllable.model.Access;
import one.superstack.controllable.pojo.Actor;

import java.io.Serializable;

public class AccessResponse implements Serializable {

    private Access access;

    private Actor actor;

    public AccessResponse() {

    }

    public AccessResponse(Access access, Actor actor) {
        this.access = access;
        this.actor = actor;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }
}
