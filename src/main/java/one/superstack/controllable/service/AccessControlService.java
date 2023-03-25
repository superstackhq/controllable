package one.superstack.controllable.service;

import one.superstack.controllable.auth.AuthenticatedActor;
import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.Access;
import one.superstack.controllable.request.AccessRequest;
import one.superstack.controllable.request.DeleteAllAccessRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessControlService {

    private final AccessService accessService;

    private final TargetService targetService;

    private final ActorService actorService;

    private final EnvironmentService environmentService;

    @Autowired
    public AccessControlService(AccessService accessService, TargetService targetService, ActorService actorService, EnvironmentService environmentService) {
        this.accessService = accessService;
        this.targetService = targetService;
        this.actorService = actorService;
        this.environmentService = environmentService;
    }

    public void add(AccessRequest accessRequest, AuthenticatedActor creator) {
        if (!targetService.exists(accessRequest.getTargetType(), accessRequest.getTargetId(), creator.getOrganizationId())) {
            throw new NotFoundException("Target not found");
        }

        if (!actorService.exists(accessRequest.getActorType(), accessRequest.getActorId(), creator.getOrganizationId())) {
            throw new NotFoundException("Actor not found");
        }

        if (null != accessRequest.getEnvironmentId() && accessRequest.getEnvironmentId().isBlank()) {
            accessRequest.setEnvironmentId(null);
        }

        if (null != accessRequest.getEnvironmentId()) {
            if (!AccessService.ALL_ENVIRONMENT.equals(accessRequest.getEnvironmentId())) {
                if (!environmentService.exists(accessRequest.getEnvironmentId(), creator.getOrganizationId())) {
                    throw new NotFoundException("Environment not found");
                }
            }
        }

        accessService.add(accessRequest, creator);
    }

    public void delete(AccessRequest accessRequest, String organizationId) {
        accessService.delete(accessRequest, organizationId);
    }

    public Access deleteAll(DeleteAllAccessRequest deleteAllAccessRequest, String organizationId) throws Throwable {
        return accessService.deleteAll(deleteAllAccessRequest, organizationId);
    }

    public List<Access> list(TargetType targetType, String targetId, ActorType actorType, String environmentId, String organizationId, Pageable pageable) {
        return accessService.list(targetType, targetId, actorType, environmentId, organizationId, pageable);
    }

    public List<Access> get(TargetType targetType, String targetId, ActorType actorType, String actorId, String environmentId, String orgnaizationId) throws Throwable {
        return List.of(accessService.get(targetType, targetId, actorType, actorId, environmentId, orgnaizationId));
    }
}
