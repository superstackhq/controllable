package one.superstack.controllable.pojo;

import one.superstack.controllable.model.PropertyValue;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PropertyValueTreeNode implements Serializable {

    private List<PropertyValue> values;

    private Map<String, PropertyValueTreeNode> children;

    public PropertyValueTreeNode() {

    }

    public PropertyValueTreeNode(List<PropertyValue> values, Map<String, PropertyValueTreeNode> children) {
        this.values = values;
        this.children = children;
    }

    public List<PropertyValue> getValues() {
        return values;
    }

    public void setValues(List<PropertyValue> values) {
        this.values = values;
    }

    public Map<String, PropertyValueTreeNode> getChildren() {
        return children;
    }

    public void setChildren(Map<String, PropertyValueTreeNode> children) {
        this.children = children;
    }
}
