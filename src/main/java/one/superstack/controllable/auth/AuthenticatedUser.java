package one.superstack.controllable.auth;

import java.io.Serializable;

public class AuthenticatedUser implements Serializable {

    private String id;

    private String organizationId;

    private Boolean admin;

    public AuthenticatedUser() {

    }

    public AuthenticatedUser(String id, String organizationId, Boolean admin) {
        this.id = id;
        this.organizationId = organizationId;
        this.admin = admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}