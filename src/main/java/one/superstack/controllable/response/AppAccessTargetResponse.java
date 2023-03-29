package one.superstack.controllable.response;

import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.model.AppAccess;
import one.superstack.controllable.pojo.Target;

import java.io.Serializable;
import java.util.Set;

public class AppAccessTargetResponse implements Serializable {

    private Target target;

    private AppAccess access;

    public AppAccessTargetResponse() {

    }

    public AppAccessTargetResponse(Target target, AppAccess access) {
        this.target = target;
        this.access = access;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public AppAccess getAccess() {
        return access;
    }

    public void setAccess(AppAccess access) {
        this.access = access;
    }
}
