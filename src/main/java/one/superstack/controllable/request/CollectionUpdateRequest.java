package one.superstack.controllable.request;

import java.io.Serializable;

public class CollectionUpdateRequest implements Serializable {

    private String name;

    private String description;

    public CollectionUpdateRequest() {

    }

    public CollectionUpdateRequest(String name, String description) {
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
