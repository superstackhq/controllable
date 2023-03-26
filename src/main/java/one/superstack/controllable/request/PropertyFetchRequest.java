package one.superstack.controllable.request;

import java.io.Serializable;
import java.util.List;

public class PropertyFetchRequest implements Serializable {

    private List<String> namespace;

    public PropertyFetchRequest() {

    }

    public PropertyFetchRequest(List<String> namespace) {
        this.namespace = namespace;
    }

    public List<String> getNamespace() {
        return namespace;
    }

    public void setNamespace(List<String> namespace) {
        this.namespace = namespace;
    }
}
