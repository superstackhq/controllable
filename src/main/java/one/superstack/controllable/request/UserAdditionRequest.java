package one.superstack.controllable.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class UserAdditionRequest implements Serializable {

    @NotBlank
    private String username;

    @NotNull
    private Boolean admin;

    public UserAdditionRequest() {

    }

    public UserAdditionRequest(String username, Boolean admin) {
        this.username = username;
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
