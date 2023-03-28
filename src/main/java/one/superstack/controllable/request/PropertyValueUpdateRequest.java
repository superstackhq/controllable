package one.superstack.controllable.request;

import one.superstack.controllable.embedded.Rule;

import java.io.Serializable;

public class PropertyValueUpdateRequest implements Serializable {

    private Object value;

    private Rule rule;

    private String changeMessage;

    public PropertyValueUpdateRequest() {

    }

    public PropertyValueUpdateRequest(Object value, Rule rule, String changeMessage) {
        this.value = value;
        this.rule = rule;
        this.changeMessage = changeMessage;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public String getChangeMessage() {
        return changeMessage;
    }

    public void setChangeMessage(String changeMessage) {
        this.changeMessage = changeMessage;
    }
}
