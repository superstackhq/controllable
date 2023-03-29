package one.superstack.controllable.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;

import java.io.Serializable;
import java.util.Set;

public class AccessRequest implements Serializable {

    @NotNull
    private TargetType targetType;

    private String targetId;

    @NotNull
    private ActorType actorType;

    @NotBlank
    private String actorId;

    private String environmentId;

    @NotEmpty
    private Set<Permission> permissions;

    public AccessRequest() {

    }

    public AccessRequest(TargetType targetType, String targetId, ActorType actorType, String actorId, String environmentId, Set<Permission> permissions) {
        this.targetType = targetType;
        this.targetId = targetId;
        this.actorType = actorType;
        this.actorId = actorId;
        this.environmentId = environmentId;
        this.permissions = permissions;
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
}
