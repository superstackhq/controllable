package one.superstack.controllable.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class ApiKeyCreationRequest implements Serializable {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Boolean hasFullAccess;

    public ApiKeyCreationRequest() {

    }

    public ApiKeyCreationRequest(String name, String description, Boolean hasFullAccess) {
        this.name = name;
        this.description = description;
        this.hasFullAccess = hasFullAccess;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHasFullAccess() {
        return hasFullAccess;
    }

    public void setHasFullAccess(Boolean hasFullAccess) {
        this.hasFullAccess = hasFullAccess;
    }
}
