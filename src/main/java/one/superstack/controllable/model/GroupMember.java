package one.superstack.controllable.model;

import one.superstack.controllable.enums.ActorType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "group_members")
@CompoundIndexes({
        @CompoundIndex(name = "group_member_mapping_index", def = "{'groupId': 1, 'userId': 1}", unique = true),
        @CompoundIndex(name = "group_member_user_index", def = "{'userId': 1}")
})
public class GroupMember implements Serializable {

    @Id
    private String id;

    private String groupId;

    private String userId;

    private ActorType creatorType;

    private String creatorId;

    private Date createdOn;

    private Date modifiedOn;

    public GroupMember() {

    }

    public GroupMember(String groupId, String userId, ActorType creatorType, String creatorId) {
        this.groupId = groupId;
        this.userId = userId;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
