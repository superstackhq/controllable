package one.superstack.controllable.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import one.superstack.controllable.enums.ActorType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "apps")
@CompoundIndexes({
        @CompoundIndex(name = "app_access_key_index", def = "{'accessKey': 1}",  unique = true)
})
public class App implements Serializable {

    @Id
    private String id;

    @TextIndexed
    private String name;

    @TextIndexed
    private String description;

    @JsonIgnore
    private String accessKey;

    private String organizationId;

    private ActorType creatorType;

    private String creatorId;

    private Date createdOn;

    private Date modifiedOn;

    @TextScore
    private Float score;

    public App() {

    }

    public App(String name, String description, String accessKey, String organizationId, ActorType creatorType, String creatorId) {
        this.name = name;
        this.description = description;
        this.accessKey = accessKey;
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

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
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

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }
}
