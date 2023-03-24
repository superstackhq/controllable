package one.superstack.controllable.request;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class AdminChangeRequest implements Serializable {

    @NotNull
    private Boolean admin;

    public AdminChangeRequest() {

    }

    public AdminChangeRequest(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
