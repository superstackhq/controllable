package one.superstack.controllable.response;

import one.superstack.controllable.enums.AffordanceType;

import java.io.Serializable;

public class AffordanceResponse implements Serializable {

    private AffordanceType type;

    private Object affordance;

    public AffordanceResponse() {

    }

    public AffordanceResponse(AffordanceType type, Object affordance) {
        this.type = type;
        this.affordance = affordance;
    }

    public AffordanceType getType() {
        return type;
    }

    public void setType(AffordanceType type) {
        this.type = type;
    }

    public Object getAffordance() {
        return affordance;
    }

    public void setAffordance(Object affordance) {
        this.affordance = affordance;
    }
}
