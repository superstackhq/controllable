package one.superstack.controllable.embedded;

import java.io.Serializable;
import java.util.List;

public class SegmentTreeStructure implements Serializable {

    private List<SegmentTreeLevel> levels;

    public SegmentTreeStructure() {

    }

    public SegmentTreeStructure(List<SegmentTreeLevel> levels) {
        this.levels = levels;
    }

    public List<SegmentTreeLevel> getLevels() {
        return levels;
    }

    public void setLevels(List<SegmentTreeLevel> levels) {
        this.levels = levels;
    }
}
