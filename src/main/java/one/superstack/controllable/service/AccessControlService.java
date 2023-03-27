package one.superstack.controllable.service;

import one.superstack.controllable.auth.AuthenticatedActor;
import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.Access;
import one.superstack.controllable.pojo.Actor;
import one.superstack.controllable.pojo.ActorReference;
import one.superstack.controllable.request.AccessRequest;
import one.superstack.controllable.request.DeleteAllAccessRequest;
import one.superstack.controllable.response.AccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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

    public List<AccessResponse> list(TargetType targetType, String targetId, ActorType actorType, String environmentId, String organizationId, Pageable pageable) throws ExecutionException, InterruptedException {
        List<Access> accesses = accessService.list(targetType, targetId, actorType, environmentId, organizationId, pageable);

        List<ActorReference> actorReferences = accesses.stream().map(access -> new ActorReference(access.getActorType(), access.getActorId())).collect(Collectors.toList());
        Map<ActorReference, Actor> actorMap = actorService.fetch(actorReferences).stream().collect(Collectors.toMap(actor -> new ActorReference(actor.getType(), actor.getReferenceId()), actor -> actor, (a, b) -> b));

        List<AccessResponse> accessResponses = new ArrayList<>();

        for (Access access : accesses) {
            accessResponses.add(new AccessResponse(access, actorMap.get(new ActorReference(access.getActorType(), access.getActorId()))));
        }

        return accessResponses;
    }

    public List<AccessResponse> get(TargetType targetType, String targetId, ActorType actorType, String actorId, String environmentId, String organizationId) throws Throwable {
        Access access = accessService.get(targetType, targetId, actorType, actorId, environmentId, organizationId);

        Actor actor = actorService.get(new ActorReference(access.getActorType(), access.getId()));
        return List.of(new AccessResponse(access, actor));
    }
}
