package one.superstack.controllable.service;

import one.superstack.controllable.auth.AuthenticatedActor;
import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.Collection;
import one.superstack.controllable.repository.CollectionRepository;
import one.superstack.controllable.request.AccessRequest;
import one.superstack.controllable.request.CollectionCreationRequest;
import one.superstack.controllable.request.CollectionUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class CollectionService {

    private final CollectionRepository collectionRepository;

    private final AccessService accessService;

    @Autowired
    public CollectionService(CollectionRepository collectionRepository, AccessService accessService) {
        this.collectionRepository = collectionRepository;
        this.accessService = accessService;
    }

    public Collection create(CollectionCreationRequest collectionCreationRequest, AuthenticatedActor creator) {
        if (nameExists(collectionCreationRequest.getName(), creator.getOrganizationId())) {
            throw new ClientException("Collection " + collectionCreationRequest.getName() + " already exists");
        }

        Collection collection = new Collection(collectionCreationRequest.getName(),
                collectionCreationRequest.getDescription(),
                creator.getOrganizationId(),
                creator.getType(),
                creator.getId());

        collection = collectionRepository.save(collection);
        accessService.add(new AccessRequest(TargetType.COLLECTION, collection.getId(), ActorType.USER, creator.getId(), AccessService.ALL_ENVIRONMENT, Set.of(Permission.ALL)), creator);
        return collection;
    }

    public List<Collection> list(String organizationId, Pageable pageable) {
        return collectionRepository.findByOrganizationId(organizationId, pageable);
    }

    public Collection get(String collectionId, String organizationId) throws Throwable {
        return collectionRepository.findByIdAndOrganizationId(collectionId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Collection not found"));
    }

    public Collection update(String collectionId, CollectionUpdateRequest collectionUpdateRequest, String organizationId) throws Throwable {
        Collection collection = get(collectionId, organizationId);

        if (null != collectionUpdateRequest.getName() && !collectionUpdateRequest.getName().isBlank()) {
            if (collectionRepository.existsByIdNotAndNameAndOrganizationId(collectionId, collectionUpdateRequest.getName(), organizationId)) {
                throw new ClientException("Collection " + collectionUpdateRequest.getName() + " already exists");
            }

            collection.setName(collectionUpdateRequest.getName());
        }

        collection.setDescription(collectionUpdateRequest.getDescription());

        collection.setModifiedOn(new Date());
        return collectionRepository.save(collection);
    }

    public Collection delete(String collectionId, String organizationId) throws Throwable {
        Collection collection = get(collectionId, organizationId);
        collectionRepository.delete(collection);
        accessService.deleteAllForTarget(TargetType.COLLECTION, collectionId);
        return collection;
    }

    public Boolean exists(String collectionId, String organizationId) {
        return collectionRepository.existsByIdAndOrganizationId(collectionId, organizationId);
    }

    private Boolean nameExists(String name, String organizationId) {
        return collectionRepository.existsByNameAndOrganizationId(name, organizationId);
    }
}
