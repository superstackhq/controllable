package one.superstack.controllable.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.util.Password;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "users")
@CompoundIndexes({
        @CompoundIndex(name = "username_index", def = "{'username': 1, 'organizationId': 1}", unique = true)
})
public class User implements Serializable {

    @Id
    private String id;

    @TextIndexed
    private String username;

    @JsonIgnore
    private String password;

    private Boolean admin;

    private String organizationId;

    private ActorType creatorType;

    private String creatorId;

    private Date createdOn;

    private Date modifiedOn;

    @TextScore
    private Float score;

    public User() {

    }

    public User(String username, String password, Boolean admin, String organizationId, ActorType creatorType, String creatorId) {
        this.username = username;
        this.password = Password.hash(password);
        this.admin = admin;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Password.hash(password);
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
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
