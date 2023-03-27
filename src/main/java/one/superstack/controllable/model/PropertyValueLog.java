package one.superstack.controllable.model;

import one.superstack.controllable.embedded.Rule;
import one.superstack.controllable.embedded.Segment;
import one.superstack.controllable.enums.ChangeType;
import one.superstack.controllable.enums.PropertyActorType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "property_value_logs")
public class PropertyValueLog implements Serializable {

    @Id
    private String id;

    private ChangeType changeType;

    private String changeMessage;

    private String propertyValueId;

    private String propertyId;

    private String environmentId;

    private Segment segment;

    private Rule rule;

    private Object value;

    private String organizationId;

    private PropertyActorType creatorType;

    private String creatorId;

    private Date createdOn;

    private Date modifiedOn;

    public PropertyValueLog() {

    }

    public PropertyValueLog(ChangeType changeType, String changeMessage, String propertyValueId, String propertyId, String environmentId, Segment segment, Rule rule, Object value, String organizationId, PropertyActorType creatorType, String creatorId) {
        this.changeType = changeType;
        this.changeMessage = changeMessage;
        this.propertyValueId = propertyValueId;
        this.propertyId = propertyId;
        this.environmentId = environmentId;
        this.segment = segment;
        this.rule = rule;
        this.value = value;
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

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public String getChangeMessage() {
        return changeMessage;
    }

    public void setChangeMessage(String changeMessage) {
        this.changeMessage = changeMessage;
    }

    public String getPropertyValueId() {
        return propertyValueId;
    }

    public void setPropertyValueId(String propertyValueId) {
        this.propertyValueId = propertyValueId;
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
