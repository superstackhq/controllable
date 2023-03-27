package one.superstack.controllable.pojo;

import one.superstack.controllable.enums.TargetType;

import java.io.Serializable;

public class Target implements Serializable {

    private TargetType type;

    private String referenceId;

    private Object data;

    public Target() {

    }

    public Target(TargetType type, String referenceId, Object data) {
        this.type = type;
        this.referenceId = referenceId;
        this.data = data;
    }

    public TargetType getType() {
        return type;
    }

    public void setType(TargetType type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
