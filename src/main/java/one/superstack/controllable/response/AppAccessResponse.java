package one.superstack.controllable.response;

import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.model.App;

import java.io.Serializable;
import java.util.Set;

public class AppAccessResponse implements Serializable {

    private App app;

    private Set<Permission> permissions;

    public AppAccessResponse() {

    }

    public AppAccessResponse(App app, Set<Permission> permissions) {
        this.app = app;
        this.permissions = permissions;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
