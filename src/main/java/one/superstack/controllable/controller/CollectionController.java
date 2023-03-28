package one.superstack.controllable.controller;

import jakarta.validation.Valid;
import one.superstack.controllable.auth.AuthenticatedController;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.model.Collection;
import one.superstack.controllable.request.CollectionCreationRequest;
import one.superstack.controllable.request.CollectionUpdateRequest;
import one.superstack.controllable.service.AccessService;
import one.superstack.controllable.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class CollectionController extends AuthenticatedController {

    private final CollectionService collectionService;

    private final AccessService accessService;

    @Autowired
    public CollectionController(CollectionService collectionService, AccessService accessService) {
        this.collectionService = collectionService;
        this.accessService = accessService;
    }

    @PostMapping(value = "/collections")
    public Collection create(@Valid @RequestBody CollectionCreationRequest collectionCreationRequest) {
        checkAccess(accessService, TargetType.COLLECTION, null, Permission.CREATE);
        return collectionService.create(collectionCreationRequest, getActor());
    }

    @GetMapping(value = "/collections")
    public List<Collection> list(Pageable pageable) {
        return collectionService.list(getOrganizationId(), pageable);
    }

    @GetMapping(value = "/collections/{collectionId}")
    public Collection get(@PathVariable String collectionId) throws Throwable {
        return collectionService.get(collectionId, getOrganizationId());
    }

    @PutMapping(value = "/collections/{collectionId}")
    public Collection update(@PathVariable String collectionId, @Valid @RequestBody CollectionUpdateRequest collectionUpdateRequest) throws Throwable {
        checkAccess(accessService, TargetType.COLLECTION, collectionId, Permission.UPDATE);
        return collectionService.update(collectionId, collectionUpdateRequest, getOrganizationId());
    }

    @DeleteMapping(value = "/collections/{collectionId}")
    public Collection delete(@PathVariable String collectionId) throws Throwable {
        checkAccess(accessService, TargetType.COLLECTION, collectionId, Permission.DELETE);
        return collectionService.delete(collectionId, getOrganizationId());
    }

    @GetMapping(value = "/collections/search")
    public List<Collection> search(@RequestParam String query, Pageable pageable) {
        return collectionService.search(query, getOrganizationId(), pageable);
    }
}
