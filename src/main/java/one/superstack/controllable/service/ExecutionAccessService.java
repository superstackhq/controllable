package one.superstack.controllable.service;

import one.superstack.controllable.enums.AffordanceType;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.model.AppAccess;
import one.superstack.controllable.model.CollectionMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExecutionAccessService {

    private final AppAccessService appAccessService;

    private final CollectionMemberService collectionMemberService;

    @Autowired
    public ExecutionAccessService(AppAccessService appAccessService, CollectionMemberService collectionMemberService) {
        this.appAccessService = appAccessService;
        this.collectionMemberService = collectionMemberService;
    }

    public Boolean hasPermission(Set<String> propertyIds, String appId, Permission permission) {
        // To track property ids still without permission
        Set<String> propertyIdsWithoutPermission = new HashSet<>(propertyIds);

        // Find properties having direct permission
        List<AppAccess> directPropertyAccesses = appAccessService.filterTargetsWithPermission(appId, TargetType.PROPERTY, propertyIds, permission);
        directPropertyAccesses.stream().map(AppAccess::getTargetId).toList().forEach(propertyIdsWithoutPermission::remove);

        if (propertyIdsWithoutPermission.isEmpty()) {
            return true;
        }

        // Find collections of the properties
        List<CollectionMember> collectionMemberMappings = collectionMemberService.listCollections(AffordanceType.PROPERTY, propertyIdsWithoutPermission);

        if (collectionMemberMappings.isEmpty()) {
            return true;
        }

        // Construct a map of property ids without permissions to their collections
        Map<String, Set<String>> propertyCollectionIdsMap = propertyIdsWithoutPermission.stream().collect(Collectors.toMap(propertyIdWithoutPermission -> propertyIdWithoutPermission, propertyIdWithoutPermission -> new HashSet<>(), (a, b) -> b));
        Set<String> collectionIds = new HashSet<>();

        for (CollectionMember collectionMemberMapping : collectionMemberMappings) {
            propertyCollectionIdsMap.get(collectionMemberMapping.getAffordanceId()).add(collectionMemberMapping.getCollectionId());
            collectionIds.add(collectionMemberMapping.getCollectionId());
        }

        for (Map.Entry<String, Set<String>> propertyCollectionIdsMapEntry : propertyCollectionIdsMap.entrySet()) {
            // If some property id without permission has no collection associated, return early
            if (propertyCollectionIdsMapEntry.getValue().isEmpty()) {
                return false;
            }
        }

        // Fetch the collection app access for the collections
        List<AppAccess> appCollectionAccesses = appAccessService.filterTargetsWithPermission(appId, TargetType.COLLECTION, collectionIds, permission);
        Map<String, Boolean> collectionsWithAppAccess = appCollectionAccesses.stream().collect(Collectors.toMap(AppAccess::getTargetId, appCollectionAccess -> true, (a, b) -> b));

        // Check if app has access to at least one associated collection of the property
        for (Map.Entry<String, Set<String>> propertyCollectionIdsMapEntry : propertyCollectionIdsMap.entrySet()) {
            String propertyIdWithoutPermission = propertyCollectionIdsMapEntry.getKey();
            Set<String> associatedCollectionIds = propertyCollectionIdsMapEntry.getValue();

            boolean associatedCollectionIdWithAppAccessExists = false;

            for (String associatedCollectionId : associatedCollectionIds) {
                if (collectionsWithAppAccess.containsKey(associatedCollectionId)) {
                    associatedCollectionIdWithAppAccessExists = true;
                    break;
                }
            }

            if (!associatedCollectionIdWithAppAccessExists) {
                // Access absent in all associated collections
                return false;
            } else {
                // Access present in at least one associated collection
                propertyIdsWithoutPermission.remove(propertyIdWithoutPermission);
            }
        }

        return propertyIdsWithoutPermission.size() == 0;
    }
}
