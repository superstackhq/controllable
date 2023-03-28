package one.superstack.controllable.auth.actor;

import one.superstack.controllable.enums.ActorType;

import java.io.Serializable;

public class AuthenticatedActor implements Serializable {

    private ActorType type;

    private String id;

    private String organizationId;

    private Boolean hasFullAccess;

    public AuthenticatedActor() {

    }

    public AuthenticatedActor(ActorType type, String id, String organizationId, Boolean hasFullAccess) {
        this.type = type;
        this.id = id;
        this.organizationId = organizationId;
        this.hasFullAccess = hasFullAccess;
    }

    public ActorType getType() {
        return type;
    }

    public void setType(ActorType type) {
        this.type = type;
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

    public Boolean getHasFullAccess() {
        return hasFullAccess;
    }

    public void setHasFullAccess(Boolean hasFullAccess) {
        this.hasFullAccess = hasFullAccess;
    }
}