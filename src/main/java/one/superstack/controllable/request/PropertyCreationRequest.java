package one.superstack.controllable.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import one.superstack.controllable.embedded.Constraints;
import one.superstack.controllable.embedded.SegmentTreeStructure;
import one.superstack.controllable.enums.DataType;

import java.io.Serializable;
import java.util.List;

public class PropertyCreationRequest implements Serializable {

    private List<String> namespace;

    @NotBlank
    private String key;

    private String version;

    private String description;

    @NotNull
    private DataType dataType;

    private SegmentTreeStructure segmentTreeStructure;

    private Constraints constraints;

    public PropertyCreationRequest(List<String> namespace, String key, String version, String description, DataType dataType, SegmentTreeStructure segmentTreeStructure, Constraints constraints) {
        this.namespace = namespace;
        this.key = key;
        this.version = version;
        this.description = description;
        this.dataType = dataType;
        this.segmentTreeStructure = segmentTreeStructure;
        this.constraints = constraints;
    }

    public List<String> getNamespace() {
        return namespace;
    }

    public void setNamespace(List<String> namespace) {
        this.namespace = namespace;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public SegmentTreeStructure getSegmentTreeStructure() {
        return segmentTreeStructure;
    }

    public void setSegmentTreeStructure(SegmentTreeStructure segmentTreeStructure) {
        this.segmentTreeStructure = segmentTreeStructure;
    }

    public Constraints getConstraints() {
        return constraints;
    }

    public void setConstraints(Constraints constraints) {
        this.constraints = constraints;
    }
}
