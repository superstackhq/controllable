package one.superstack.controllable.request;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class PasswordChangeRequest implements Serializable {

    @NotBlank
    private String password;

    public PasswordChangeRequest() {

    }

    public PasswordChangeRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
