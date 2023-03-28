package one.superstack.controllable.service;

import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.enums.AffordanceType;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.model.CollectionMember;
import one.superstack.controllable.pojo.Affordance;
import one.superstack.controllable.util.AffordanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AffordanceAccessService {

    private final CollectionMemberService collectionMemberService;

    private final AccessService accessService;

    @Autowired
    public AffordanceAccessService(CollectionMemberService collectionMemberService, AccessService accessService) {
        this.collectionMemberService = collectionMemberService;
        this.accessService = accessService;
    }

    public Boolean hasPermissionOnEnvironment(TargetType targetType, String targetId, ActorType actorType, String actorId, String environmentId, Permission permission, Boolean hasFullAccess) {
        if (!TargetType.PROPERTY.equals(targetType)) {
            throw new ClientException("Invalid target type");
        }

        if (accessService.hasPermissionOnEnvironment(targetType, targetId, actorType, actorId, environmentId, permission, hasFullAccess)) {
            return true;
        }

        AffordanceType affordanceType = AffordanceUtil.convert(targetType);

        List<CollectionMember> collectionMembers = collectionMemberService.listCollections(new Affordance(affordanceType, targetId));

        if (collectionMembers.isEmpty()) {
            return false;
        }

        Set<String> collectionIds = collectionMembers.stream().map(CollectionMember::getCollectionId).collect(Collectors.toSet());
        return accessService.hasPermissionOnAtLeastOneTargetOnEnvironment(TargetType.COLLECTION, collectionIds, actorType, actorId, environmentId, permission, hasFullAccess);
    }
}
