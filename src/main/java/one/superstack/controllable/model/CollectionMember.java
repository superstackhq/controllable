package one.superstack.controllable.model;

import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.enums.AffordanceType;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "collection_members")
@CompoundIndexes({
        @CompoundIndex(name = "collection_member_mapping_index", def = "{'collectionId': 1, 'affordanceType': 1, 'affordanceId': 1}", unique = true),
        @CompoundIndex(name = "collection_member_affordance_index", def = "{'affordanceType': 1, 'affordanceId': 1}")
})
public class CollectionMember implements Serializable {

    private String id;

    private String collectionId;

    private AffordanceType affordanceType;

    private String affordanceId;

    private String organizationId;

    private ActorType creatorType;

    private String creatorId;

    private Date createdOn;

    private Date modifiedOn;

    public CollectionMember() {
    }

    public CollectionMember(String collectionId, AffordanceType affordanceType, String affordanceId, String organizationId, ActorType creatorType, String creatorId) {
        this.collectionId = collectionId;
        this.affordanceType = affordanceType;
        this.affordanceId = affordanceId;
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

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public AffordanceType getAffordanceType() {
        return affordanceType;
    }

    public void setAffordanceType(AffordanceType affordanceType) {
        this.affordanceType = affordanceType;
    }

    public String getAffordanceId() {
        return affordanceId;
    }

    public void setAffordanceId(String affordanceId) {
        this.affordanceId = affordanceId;
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
