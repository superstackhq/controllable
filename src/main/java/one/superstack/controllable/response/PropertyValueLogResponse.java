package one.superstack.controllable.response;

import one.superstack.controllable.model.PropertyValueLog;
import one.superstack.controllable.pojo.PropertyActor;

import java.io.Serializable;

public class PropertyValueLogResponse implements Serializable {

    private PropertyValueLog log;

    private PropertyActor actor;

    public PropertyValueLogResponse() {

    }

    public PropertyValueLogResponse(PropertyValueLog log, PropertyActor actor) {
        this.log = log;
        this.actor = actor;
    }

    public PropertyValueLog getLog() {
        return log;
    }

    public void setLog(PropertyValueLog log) {
        this.log = log;
    }

    public PropertyActor getActor() {
        return actor;
    }

    public void setActor(PropertyActor actor) {
        this.actor = actor;
    }
}
