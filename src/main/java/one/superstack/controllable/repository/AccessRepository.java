package one.superstack.controllable.repository;

import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.model.Access;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AccessRepository extends MongoRepository<Access, String> {

    Optional<Access> findByTargetTypeAndTargetIdAndActorTypeAndActorIdAndEnvironmentIdAndOrganizationId(TargetType targetType, String targetId, ActorType actorType, String actorId, String environmentId, String organizationId);

    List<Access> findByTargetTypeAndTargetIdAndActorTypeAndEnvironmentIdAndOrganizationId(TargetType targetType, String targetId, ActorType actorType, String environmentId, String organizationId, Pageable pageable);

    Boolean existsByTargetTypeAndTargetIdAndActorTypeAndActorIdAndEnvironmentIdInAndPermissionsIn(TargetType targetType, String targetId, ActorType actorType, String actorId, Set<String> environmentIds, Set<Permission> permissions);

    Boolean existsByTargetTypeAndTargetIdAndActorTypeAndActorIdInAndEnvironmentIdInAndPermissionsIn(TargetType targetType, String targetId, ActorType actorType, Set<String> actorIds, Set<String> environmentIds, Set<Permission> permissions);

    Boolean existsByTargetTypeAndTargetIdInAndActorTypeAndActorIdAndEnvironmentIdInAndPermissionsIn(TargetType targetType, Set<String> targetIds, ActorType actorType, String actorId, Set<String> environmentIds, Set<Permission> permissions);

    Boolean existsByTargetTypeAndTargetIdInAndActorTypeAndActorIdInAndEnvironmentIdInAndPermissionsIn(TargetType targetType, Set<String> targetIds, ActorType actorType, Set<String> actorIds, Set<String> environmentIds, Set<Permission> permissions);
}
