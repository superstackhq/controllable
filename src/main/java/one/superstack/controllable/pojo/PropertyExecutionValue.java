package one.superstack.controllable.pojo;

import one.superstack.controllable.embedded.Rule;
import one.superstack.controllable.embedded.Segment;
import one.superstack.controllable.model.PropertyValue;
import one.superstack.controllable.response.PropertyExecutionResponse;

import java.io.Serializable;

public class PropertyExecutionValue implements Serializable {

    private String id;

    private Object data;

    private Rule rule;

    private Segment segment;

    public PropertyExecutionValue() {

    }

    public PropertyExecutionValue(PropertyValue value) {
        this.id = value.getId();
        this.data = value.getValue();
        this.rule = value.getRule();
        this.segment = value.getSegment();
    }

    public PropertyExecutionValue(String id, Object data, Rule rule, Segment segment) {
        this.id = id;
        this.data = data;
        this.rule = rule;
        this.segment = segment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }
}
