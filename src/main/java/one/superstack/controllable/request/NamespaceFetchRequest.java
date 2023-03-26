package one.superstack.controllable.request;

import java.io.Serializable;
import java.util.List;

public class NamespaceFetchRequest implements Serializable {

    private List<String> parent;

    public NamespaceFetchRequest() {

    }

    public NamespaceFetchRequest(List<String> parent) {
        this.parent = parent;
    }

    public List<String> getParent() {
        return parent;
    }

    public void setParent(List<String> parent) {
        this.parent = parent;
    }
}
