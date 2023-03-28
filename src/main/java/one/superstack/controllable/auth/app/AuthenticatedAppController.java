package one.superstack.controllable.auth.app;

public class AuthenticatedAppController implements RequiresAppAuthentication {

    public AuthenticatedApp getApp() {
        return ThreadLocalWrapper.getApp();
    }

    public String getAppId() {
        return getApp().getId();
    }

    public String getOrganizationId() {
        return getApp().getOrganizationId();
    }
}
