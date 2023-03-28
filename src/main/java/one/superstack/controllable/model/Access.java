package one.superstack.controllable.model;

import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Document(collection = "accesses")
@CompoundIndexes({
        @CompoundIndex(name = "access_mapping_index", def = "{'targetType': 1, 'targetId': 1, 'actorType': 1, 'actorId': 1, 'environmentId': 1}", unique = true)
})
public class Access implements Serializable {

    @Id
    private String id;

    private ActorType actorType;

    private String actorId;

    private TargetType targetType;

    private String targetId;

    private String environmentId;

    private Set<Permission> permissions;

    private String organizationId;

    private ActorType creatorType;

    private String creatorId;

    private Date createdOn;

    private Date modifiedOn;

    public Access() {

    }

    public Access(ActorType actorType, String actorId, TargetType targetType, String targetId, String environmentId, Set<Permission> permissions, String organizationId, ActorType creatorType, String creatorId) {
        this.actorType = actorType;
        this.actorId = actorId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.environmentId = environmentId;
        this.permissions = permissions;
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

    public ActorType getActorType() {
        return actorType;
    }

    public void setActorType(ActorType actorType) {
        this.actorType = actorType;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(String environmentId) {
        this.environmentId = environmentId;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
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

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
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
