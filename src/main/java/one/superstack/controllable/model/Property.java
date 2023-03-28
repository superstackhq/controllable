package one.superstack.controllable.model;

import one.superstack.controllable.embedded.Constraints;
import one.superstack.controllable.embedded.SegmentTreeStructure;
import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.enums.DataType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document(collection = "properties")
@CompoundIndexes({
        @CompoundIndex(name = "property_reference_index", def = "{'namespace': 1, 'key': 1, 'version': 1, 'organizationId': 1}", unique = true)
})
public class Property implements Serializable {

    @Id
    private String id;

    private List<String> namespace;

    private String key;

    private String version;

    private String description;

    private DataType dataType;

    private SegmentTreeStructure segmentTreeStructure;

    private Constraints constraints;

    private String organizationId;

    private ActorType creatorType;

    private String creatorId;

    private Date createdOn;

    private Date modifiedOn;

    public Property() {
    }

    public Property(List<String> namespace, String key, String version, String description, DataType dataType, SegmentTreeStructure segmentTreeStructure, Constraints constraints, String organizationId, ActorType creatorType, String creatorId) {
        this.namespace = namespace;
        this.key = key;
        this.version = version;
        this.description = description;
        this.dataType = dataType;
        this.segmentTreeStructure = segmentTreeStructure;
        this.constraints = constraints;
        this.organizationId = organizationId;
        this.creatorType = creatorType;
        this.creatorId = creatorId;
        this.createdOn = new Date();
        this.modifiedOn = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public ActorType getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(ActorType creatorType) {
        this.creatorType = creatorType;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
