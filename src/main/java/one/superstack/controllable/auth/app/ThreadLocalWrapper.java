package one.superstack.controllable.auth.app;

public class ThreadLocalWrapper {

    private static final ThreadLocal<AuthenticatedApp> appContext;

    static {
        appContext = new ThreadLocal<>();
    }

    public static AuthenticatedApp getApp() {
        return appContext.get();
    }

    public static void setApp(AuthenticatedApp app) {
        appContext.set(app);
    }
}
