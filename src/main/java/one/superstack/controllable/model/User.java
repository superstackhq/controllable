package one.superstack.controllable.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import one.superstack.controllable.util.Password;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "users")
public class User implements Serializable {

    @Id
    private String id;

    private String username;

    @JsonIgnore
    private String password;

    private Boolean admin;

    private String organizationId;

    private String creatorId;

    private Date createdOn;

    private Date modifiedOn;

    public User() {

    }

    public User(String username, String password, Boolean admin, String organizationId, String creatorId) {
        this.username = username;
        this.password = Password.hash(password);
        this.admin = admin;
        this.organizationId = organizationId;
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
