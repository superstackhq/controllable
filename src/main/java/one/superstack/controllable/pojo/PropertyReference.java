package one.superstack.controllable.pojo;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PropertyReference implements Serializable {

    private List<String> namespace;

    @NotBlank
    private String key;

    private String version;

    public PropertyReference() {

    }

    public PropertyReference(List<String> namespace, String key, String version) {
        this.namespace = namespace;
        this.key = key;
        this.version = version;
    }

    public List<String> getNamespace() {
        return namespace;
    }

    public void setNamespace(List<String> namespace) {
        this.namespace = namespace;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String toString() {
        String namespaceString = "";
        if (null != namespace && !namespace.isEmpty()) {
            namespaceString = String.join("/", namespace);
        }

        String versionString = "";
        if (null != version) {
            versionString = version;
        }

        return String.format("%s:%s:%s", namespaceString, key, versionString);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyReference that = (PropertyReference) o;
        return Objects.equals(namespace, that.namespace) && Objects.equals(key, that.key) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, key, version);
    }
}
