package one.superstack.controllable.service;

import com.mongodb.client.result.UpdateResult;
import one.superstack.controllable.auth.actor.AuthenticatedActor;
import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.Access;
import one.superstack.controllable.model.GroupMember;
import one.superstack.controllable.repository.AccessRepository;
import one.superstack.controllable.request.AccessRequest;
import one.superstack.controllable.request.DeleteAllAccessRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class AccessService {

    public static final String ALL_ENVIRONMENT = "ALL";

    private final AccessRepository accessRepository;

    private final GroupMemberService groupMemberService;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AccessService(AccessRepository accessRepository, GroupMemberService groupMemberService, MongoTemplate mongoTemplate) {
        this.accessRepository = accessRepository;
        this.groupMemberService = groupMemberService;
        this.mongoTemplate = mongoTemplate;
    }

    public void add(AccessRequest accessRequest, AuthenticatedActor creator) {
        mongoTemplate.upsert(Query.query(Criteria
                        .where("actorType").is(accessRequest.getActorType())
                        .and("actorId").is(accessRequest.getActorId())
                        .and("targetType").is(accessRequest.getTargetType())
                        .and("targetId").is(accessRequest.getTargetId())
                        .and("environmentId").is(accessRequest.getEnvironmentId())
                        .and("organizationId").is(creator.getOrganizationId())
                        .and("deleted").is(false)),
                new Update()
                        .setOnInsert("actorType", accessRequest.getActorType())
                        .setOnInsert("actorId", accessRequest.getActorId())
                        .setOnInsert("targetType", accessRequest.getTargetType())
                        .setOnInsert("targetId", accessRequest.getTargetId())
                        .setOnInsert("environmentId", accessRequest.getEnvironmentId())
                        .setOnInsert("deleted", false)
                        .setOnInsert("creatorId", creator.getId())
                        .setOnInsert("organizationId", creator.getOrganizationId())
                        .setOnInsert("createdOn", new Date())
                        .set("modifiedOn", new Date())
                        .addToSet("permissions").each(accessRequest.getPermissions().toArray()),
                Access.class);
    }

    public Access get(TargetType targetType, String targetId, ActorType actorType, String actorId, String environmentId, String organizationId) throws Throwable {
        return accessRepository.findByTargetTypeAndTargetIdAndActorTypeAndActorIdAndEnvironmentIdAndOrganizationId(targetType, targetId, actorType, actorId, environmentId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Access not found"));
    }

    public void delete(AccessRequest accessRequest, String organizationId) {
        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(Criteria
                        .where("actorType").is(accessRequest.getActorType())
                        .and("actorId").is(accessRequest.getActorId())
                        .and("targetType").is(accessRequest.getTargetType())
                        .and("targetId").is(accessRequest.getTargetId())
                        .and("environmentId").is(accessRequest.getEnvironmentId())
                        .and("organizationId").is(organizationId)
                        .and("deleted").is(false)),
                new Update()
                        .set("modifiedOn", new Date())
                        .pullAll("permissions", accessRequest.getPermissions().toArray()),
                Access.class);

        if (updateResult.getMatchedCount() == 0) {
            throw new NotFoundException("Access not found");
        }
    }

    public Access deleteAll(DeleteAllAccessRequest deleteAllAccessRequest, String organizationId) throws Throwable {
        Access access = get(deleteAllAccessRequest.getTargetType(), deleteAllAccessRequest.getTargetId(), deleteAllAccessRequest.getActorType(), deleteAllAccessRequest.getActorId(), deleteAllAccessRequest.getEnvironmentId(), organizationId);
        accessRepository.delete(access);
        return access;
    }

    public List<Access> list(TargetType targetType, String targetId, ActorType actorType, String environmentId, String organizationId, Pageable pageable) {
        return accessRepository.findByTargetTypeAndTargetIdAndActorTypeAndEnvironmentIdAndOrganizationId(targetType, targetId, actorType, environmentId, organizationId, pageable);
    }

    @Async
    public void deleteAllForTarget(TargetType targetType, String targetId) {
        mongoTemplate.remove(Query.query(Criteria.where("targetType").is(targetType).and("targetId").is(targetId)), Access.class);
    }

    @Async
    public void deleteAllForActor(ActorType actorType, String actorId) {
        mongoTemplate.remove(Query.query(Criteria.where("actorType").is(actorType).and("actorId").is(actorId)), Access.class);
    }

    @Async
    public void deleteAllForEnvironment(String environmentId) {
        mongoTemplate.remove(Query.query(Criteria.where("environmentId").is(environmentId)), Access.class);
    }

    public Boolean hasPermission(TargetType targetType, String targetId, ActorType actorType, String actorId, Permission permission, Boolean hasFullAccess) {
        return hasPermissionOnEnvironment(targetType, targetId, actorType, actorId, null, permission, hasFullAccess);
    }

    public Boolean hasPermissionOnEnvironment(TargetType targetType, String targetId, ActorType actorType, String actorId, String environmentId, Permission permission, Boolean hasFullAccess) {
        if (hasFullAccess) {
            return true;
        }

        switch (actorType) {
            case USER -> {
                return checkPermissionForUserOnEnvironment(targetType, targetId, actorId, environmentId, permission);
            }

            case API_KEY -> {
                return checkPermissionForApiKeyOnEnvironment(targetType, targetId, actorId, environmentId, permission);
            }

            default -> throw new ClientException("Invalid actor type");
        }
    }

    public Boolean hasPermissionOnAtLeastOneTargetOnEnvironment(TargetType targetType, Set<String> targetIds, ActorType actorType, String actorId, String environmentId, Permission permission, Boolean hasFullAccess) {
        if (hasFullAccess) {
            return true;
        }

        switch (actorType) {
            case USER -> {
                return checkPermissionForUserOnAtLeastOneTargetOnEnvironment(targetType, targetIds, actorId, environmentId, permission);
            }

            case API_KEY -> {
                return checkPermissionForApiKeyOnAtLeastOneTargetOnEnvironment(targetType, targetIds, actorId, environmentId, permission);
            }

            default -> throw new ClientException("Invalid actor type");
        }
    }

    private Boolean checkPermissionForUserOnEnvironment(TargetType targetType, String targetId, String userId, String environmentId, Permission permission) {
        Set<String> environmentIds = Set.of(environmentId, ALL_ENVIRONMENT);
        Set<Permission> permissions = Set.of(Permission.ALL, permission);

        if (accessRepository.existsByTargetTypeAndTargetIdAndActorTypeAndActorIdAndEnvironmentIdInAndPermissionsIn(targetType, targetId, ActorType.USER, userId, environmentIds, permissions)) {
            return true;
        }

        Set<String> groupIds = groupMemberService.listGroups(userId, Pageable.unpaged()).stream().map(GroupMember::getGroupId).collect(Collectors.toSet());
        if (groupIds.isEmpty()) {
            return false;
        }

        return accessRepository.existsByTargetTypeAndTargetIdAndActorTypeAndActorIdInAndEnvironmentIdInAndPermissionsIn(targetType, targetId, ActorType.GROUP, groupIds, environmentIds, permissions);
    }

    private Boolean checkPermissionForApiKeyOnEnvironment(TargetType targetType, String targetId, String apiKeyId, String environmentId, Permission permission) {
        Set<String> environmentIds = Set.of(environmentId, ALL_ENVIRONMENT);
        Set<Permission> permissions = Set.of(Permission.ALL, permission);
        return accessRepository.existsByTargetTypeAndTargetIdAndActorTypeAndActorIdAndEnvironmentIdInAndPermissionsIn(targetType, targetId, ActorType.API_KEY, apiKeyId, environmentIds, permissions);
    }

    private Boolean checkPermissionForUserOnAtLeastOneTargetOnEnvironment(TargetType targetType, Set<String> targetIds, String userId, String environmentId, Permission permission) {
        Set<String> environmentIds = Set.of(environmentId, ALL_ENVIRONMENT);
        Set<Permission> permissions = Set.of(Permission.ALL, permission);

        if (accessRepository.existsByTargetTypeAndTargetIdInAndActorTypeAndActorIdAndEnvironmentIdInAndPermissionsIn(targetType, targetIds, ActorType.USER, userId, environmentIds, permissions)) {
            return true;
        }

        Set<String> groupIds = groupMemberService.listGroups(userId, Pageable.unpaged()).stream().map(GroupMember::getGroupId).collect(Collectors.toSet());
        if (groupIds.isEmpty()) {
            return false;
        }

        return accessRepository.existsByTargetTypeAndTargetIdInAndActorTypeAndActorIdInAndEnvironmentIdInAndPermissionsIn(targetType, targetIds, ActorType.GROUP, groupIds, environmentIds, permissions);
    }

    private Boolean checkPermissionForApiKeyOnAtLeastOneTargetOnEnvironment(TargetType targetType, Set<String> targetIds, String apiKeyId, String environmentId, Permission permission) {
        Set<String> environmentIds = Set.of(environmentId, ALL_ENVIRONMENT);
        Set<Permission> permissions = Set.of(Permission.ALL, permission);
        return accessRepository.existsByTargetTypeAndTargetIdInAndActorTypeAndActorIdAndEnvironmentIdInAndPermissionsIn(targetType, targetIds, ActorType.API_KEY, apiKeyId, environmentIds, permissions);
    }
}
