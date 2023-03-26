package one.superstack.controllable.request;

import java.io.Serializable;

public class PropertyUpdateRequest implements Serializable {

    private String description;

    public PropertyUpdateRequest() {

    }

    public PropertyUpdateRequest(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
