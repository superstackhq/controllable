package one.superstack.controllable.response;

import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.pojo.Target;

import java.io.Serializable;
import java.util.Set;

public class TargetResponse implements Serializable {


    private Target target;

    private Set<Permission> permissions;

    public TargetResponse() {

    }

    public TargetResponse(Target target, Set<Permission> permissions) {
        this.target = target;
        this.permissions = permissions;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
