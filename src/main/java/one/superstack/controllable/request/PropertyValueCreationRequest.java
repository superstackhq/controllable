package one.superstack.controllable.request;

import one.superstack.controllable.embedded.Rule;
import one.superstack.controllable.embedded.Segment;

import java.io.Serializable;

public class PropertyValueCreationRequest implements Serializable {

    private Segment segment;

    private Rule rule;

    private Object value;

    private String changeMessage;

    public PropertyValueCreationRequest() {

    }

    public PropertyValueCreationRequest(Segment segment, Rule rule, Object value, String changeMessage) {
        this.segment = segment;
        this.rule = rule;
        this.value = value;
        this.changeMessage = changeMessage;
    }

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getChangeMessage() {
        return changeMessage;
    }

    public void setChangeMessage(String changeMessage) {
        this.changeMessage = changeMessage;
    }
}
