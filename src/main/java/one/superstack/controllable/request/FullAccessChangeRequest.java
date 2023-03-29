package one.superstack.controllable.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class FullAccessChangeRequest implements Serializable {

    @NotNull
    @JsonProperty("hasFullAccess")
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
