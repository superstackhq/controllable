package one.superstack.controllable.request;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class CollectionCreationRequest implements Serializable {

    @NotBlank
    private String name;

    private String description;

    public CollectionCreationRequest() {

    }

    public CollectionCreationRequest(String name, String description) {
        this.name = name;
        this.description = description;
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
}
