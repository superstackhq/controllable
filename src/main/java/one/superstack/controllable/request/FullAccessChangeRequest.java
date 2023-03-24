package one.superstack.controllable.request;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class FullAccessChangeRequest implements Serializable {

    @NotNull
    private Boolean hasFullAccess;

    public FullAccessChangeRequest() {

    }

    public FullAccessChangeRequest(Boolean hasFullAccess) {
        this.hasFullAccess = hasFullAccess;
    }

    public Boolean getHasFullAccess() {
        return hasFullAccess;
    }

    public void setHasFullAccess(Boolean hasFullAccess) {
        this.hasFullAccess = hasFullAccess;
    }
}
