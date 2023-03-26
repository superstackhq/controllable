package one.superstack.controllable.embedded;

import one.superstack.controllable.enums.DataType;

import java.io.Serializable;

public class SegmentTreeLevel implements Serializable {

    private String name;

    private String description;

    private DataType dataType;

    public SegmentTreeLevel() {

    }

    public SegmentTreeLevel(String name, String description, DataType dataType) {
        this.name = name;
        this.description = description;
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}
