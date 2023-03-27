package one.superstack.controllable.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;

import java.io.Serializable;
import java.util.Set;

public class AppAccessRequest implements Serializable {

    @NotNull
    private TargetType targetType;

    @NotBlank
    private String targetId;

    @NotEmpty
    private Set<Permission> permissions;

    public AppAccessRequest() {

    }

    public AppAccessRequest(TargetType targetType, String targetId, Set<Permission> permissions) {
        this.targetType = targetType;
        this.targetId = targetId;
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

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
