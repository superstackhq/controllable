package one.superstack.controllable.request;

import java.io.Serializable;

public class ApiKeyUpdateRequest implements Serializable {

    private String name;

    private String description;

    public ApiKeyUpdateRequest() {
    }

    public ApiKeyUpdateRequest(String name, String description) {
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
