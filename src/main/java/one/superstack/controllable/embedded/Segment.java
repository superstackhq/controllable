package one.superstack.controllable.embedded;

import java.io.Serializable;
import java.util.List;

public class Segment implements Serializable {

    private List<SegmentPathComponent> path;

    public Segment() {

    }

    public Segment(List<SegmentPathComponent> path) {
        this.path = path;
    }

    public List<SegmentPathComponent> getPath() {
        return path;
    }

    public void setPath(List<SegmentPathComponent> path) {
        this.path = path;
    }
}
