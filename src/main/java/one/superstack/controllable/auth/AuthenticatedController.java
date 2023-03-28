package one.superstack.controllable.auth;

import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.exception.NotAllowedException;
import one.superstack.controllable.service.AccessService;
import one.superstack.controllable.service.AffordanceAccessService;

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

    public void checkAccess(AccessService accessService, TargetType targetType, String targetId, Permission permission) {
        if (!accessService.hasPermission(targetType, targetId, getActorType(), getActorId(), permission, hasFullAccess())) {
            throw new NotAllowedException();
        }
    }

    public void checkAccess(AccessService accessService, TargetType targetType, String targetId, String environmentId, Permission permission) {
        if (!accessService.hasPermissionOnEnvironment(targetType, targetId, getActorType(), getActorId(), environmentId, permission, hasFullAccess())) {
            throw new NotAllowedException();
        }
    }

    public void checkAccess(AffordanceAccessService accessService, TargetType targetType, String targetId, String environmentId, Permission permission) {
        if (!accessService.hasPermissionOnEnvironment(targetType, targetId, getActorType(), getActorId(), environmentId, permission, hasFullAccess())) {
            throw new NotAllowedException();
        }
    }
}