package one.superstack.controllable.request;

import one.superstack.controllable.embedded.Segment;

import java.io.Serializable;

public class PropertyValueFetchRequest implements Serializable {

    private Segment segment;

    public PropertyValueFetchRequest() {

    }

    public PropertyValueFetchRequest(Segment segment) {
        this.segment = segment;
    }

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }
}
