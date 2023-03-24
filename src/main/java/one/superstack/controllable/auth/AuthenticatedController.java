package one.superstack.controllable.auth;

import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.exception.NotAllowedException;

public class AuthenticatedController implements RequiresAuthentication {

    public AuthenticatedActor getActor() {
        return ThreadLocalWrapper.getActor();
    }

    public String getActorId() {
        return getActor().getId();
    }

    public ActorType getActorType() {
        return getActor().getType();
    }

    public String getOrganizationId() {
        return getActor().getOrganizationId();
    }

    public Boolean hasFullAccess() {
        return getActor().getHasFullAccess();
    }

    public void checkFullAccess() {
        if (!hasFullAccess()) {
            throw new NotAllowedException();
        }
    }
}