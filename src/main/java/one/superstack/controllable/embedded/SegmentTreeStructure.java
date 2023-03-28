package one.superstack.controllable.embedded;

import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.util.DataTypeValidator;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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

    public Segment validateSegment(Segment segment) {
        if (this.levels == null || this.levels.size() == 0) {
            return null;
        }

        Map<String, Object> segmentMap = new HashMap<>();

        if (null != segment && null != segment.getPath()) {
            segmentMap = segment.getPath().stream().collect(Collectors.toMap(SegmentPathComponent::getName, SegmentPathComponent::getValue, (a, b) -> b));
        }

        List<SegmentPathComponent> validatedSegmentPathComponents = new ArrayList<>();

        for (SegmentTreeLevel segmentTreeLevelDefinition : this.levels) {
            Object segmentData = segmentMap.get(segmentTreeLevelDefinition.getName());

            if (null != segmentData) {
                if (!DataTypeValidator.validate(segmentTreeLevelDefinition.getDataType(), segmentData)) {
                    throw new ClientException("Invalid data type for segment path component " + segmentTreeLevelDefinition.getName());
                }
            }

            validatedSegmentPathComponents.add(new SegmentPathComponent(segmentTreeLevelDefinition.getName(), segmentData));
        }

        return new Segment(validatedSegmentPathComponents);
    }
}
