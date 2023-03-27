package one.superstack.controllable.model;

import one.superstack.controllable.embedded.Rule;
import one.superstack.controllable.embedded.Segment;
import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.enums.PropertyActorType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "property_values")
public class PropertyValue implements Serializable {

    @Id
    private String id;

    private String propertyId;

    private String environmentId;

    private Object value;

    private Segment segment;

    private Rule rule;

    private String organizationId;

    private PropertyActorType creatorType;

    private String creatorId;

    private Date createdOn;

    private Date modifiedOn;

    public PropertyValue() {

    }

    public PropertyValue(String propertyId, String environmentId, Object value, Segment segment, Rule rule, String organizationId, PropertyActorType creatorType, String creatorId) {
        this.propertyId = propertyId;
        this.environmentId = environmentId;
        this.value = value;
        this.segment = segment;
        this.rule = rule;
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

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(String environmentId) {
        this.environmentId = environmentId;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
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

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public PropertyActorType getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(PropertyActorType creatorType) {
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
