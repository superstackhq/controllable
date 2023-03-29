package one.superstack.controllable.controller;

import jakarta.validation.Valid;
import one.superstack.controllable.auth.actor.AuthenticatedController;
import one.superstack.controllable.enums.AffordanceType;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.model.Collection;
import one.superstack.controllable.pojo.Affordance;
import one.superstack.controllable.response.AffordanceResponse;
import one.superstack.controllable.response.SuccessResponse;
import one.superstack.controllable.service.AccessService;
import one.superstack.controllable.service.CollectionMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class CollectionMemberController extends AuthenticatedController {

    private final CollectionMemberService collectionMemberService;

    private final AccessService accessService;

    @Autowired
    public CollectionMemberController(CollectionMemberService collectionMemberService, AccessService accessService) {
        this.collectionMemberService = collectionMemberService;
        this.accessService = accessService;
    }

    @PostMapping(value = "/collections/{collectionId}/members")
    public SuccessResponse add(@PathVariable String collectionId, @Valid @RequestBody Affordance affordance) {
        checkAccess(accessService, TargetType.COLLECTION, collectionId, Permission.MANAGE_MEMBERS);
        checkCollectionManagementAccessOnAffordance(affordance);
        collectionMemberService.add(collectionId, affordance, getActor());
        return new SuccessResponse("Collection member added successfully");
    }

    @DeleteMapping(value = "/collections/{collectionId}/members")
    public SuccessResponse delete(@PathVariable String collectionId, @Valid @RequestBody Affordance affordance) {
        checkAccess(accessService, TargetType.COLLECTION, collectionId, Permission.MANAGE_MEMBERS);
        checkCollectionManagementAccessOnAffordance(affordance);
        collectionMemberService.delete(collectionId, affordance, getOrganizationId());
        return new SuccessResponse("Collection member deleted successfully");
    }

    @GetMapping(value = "/collections/{collectionId}/members")
    public List<AffordanceResponse> listAffordances(@PathVariable String collectionId, @RequestParam AffordanceType affordanceType, Pageable pageable) {
        return collectionMemberService.listAffordances(collectionId, affordanceType, getOrganizationId(), pageable);
    }

    @GetMapping(value = "/affordances/collections")
    public List<Collection> listCollections(@RequestParam AffordanceType affordanceType, @RequestParam String affordanceId, Pageable pageable) {
        return collectionMemberService.listCollections(new Affordance(affordanceType, affordanceId), getOrganizationId(), pageable);
    }

    private void checkCollectionManagementAccessOnAffordance(Affordance affordance) {
        if (AffordanceType.PROPERTY.equals(affordance.getType())) {
            checkAccess(accessService, TargetType.PROPERTY, affordance.getReferenceId(), Permission.MANAGE_COLLECTIONS);
        } else {
            throw new ClientException("Invalid affordance type");
        }
    }
}
