package one.superstack.controllable.request;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class AuthenticationRequest implements Serializable {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String organizationName;

    public AuthenticationRequest() {

    }

    public AuthenticationRequest(String username, String password, String organizationName) {
        this.username = username;
        this.password = password;
        this.organizationName = organizationName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
