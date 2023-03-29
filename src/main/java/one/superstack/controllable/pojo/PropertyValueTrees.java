package one.superstack.controllable.pojo;

import java.io.Serializable;
import java.util.Map;

public class PropertyValueTrees implements Serializable {

    private Map<String, PropertyValueTreeNode> roots;

    public PropertyValueTrees() {

    }

    public PropertyValueTrees(Map<String, PropertyValueTreeNode> roots) {
        this.roots = roots;
    }

    public Map<String, PropertyValueTreeNode> getRoots() {
        return roots;
    }

    public void setRoots(Map<String, PropertyValueTreeNode> roots) {
        this.roots = roots;
    }
}
