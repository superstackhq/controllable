package one.superstack.controllable.response;

import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.model.App;
import one.superstack.controllable.model.AppAccess;

import java.io.Serializable;
import java.util.Set;

public class AppAccessResponse implements Serializable {

    private App app;

    private AppAccess access;

    public AppAccessResponse() {

    }

    public AppAccessResponse(App app, AppAccess access) {
        this.app = app;
        this.access = access;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public AppAccess getAccess() {
        return access;
    }

    public void setAccess(AppAccess access) {
        this.access = access;
    }
}
