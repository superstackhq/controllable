package one.superstack.controllable.service;

import com.mongodb.client.result.DeleteResult;
import one.superstack.controllable.auth.AuthenticatedActor;
import one.superstack.controllable.enums.AffordanceType;
import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.Collection;
import one.superstack.controllable.model.CollectionMember;
import one.superstack.controllable.pojo.Affordance;
import one.superstack.controllable.repository.CollectionMemberRepository;
import one.superstack.controllable.response.AffordanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CollectionMemberService {

    private final CollectionMemberRepository collectionMemberRepository;

    private final CollectionService collectionService;

    private final AffordanceService affordanceService;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public CollectionMemberService(CollectionMemberRepository collectionMemberRepository, CollectionService collectionService, AffordanceService affordanceService, MongoTemplate mongoTemplate) {
        this.collectionMemberRepository = collectionMemberRepository;
        this.collectionService = collectionService;
        this.affordanceService = affordanceService;
        this.mongoTemplate = mongoTemplate;
    }

    public void add(String collectionId, Affordance affordance, AuthenticatedActor creator) {
        if (!collectionService.exists(collectionId, creator.getOrganizationId())) {
            throw new NotFoundException("Collection not found");
        }

        if (!affordanceService.exists(affordance, creator.getOrganizationId())) {
            throw new NotFoundException("Affordance not found");
        }

        mongoTemplate.upsert(Query.query(Criteria
                        .where("affordanceType").is(affordance.getType())
                        .and("affordanceId").is(affordance.getReferenceId())
                        .and("collectionId").is(collectionId)),
                new Update()
                        .setOnInsert("affordanceType", affordance.getType())
                        .setOnInsert("affordanceId", affordance.getReferenceId())
                        .setOnInsert("collectionId", collectionId)
                        .setOnInsert("creatorType", creator.getType())
                        .setOnInsert("creatorId", creator.getId())
                        .setOnInsert("createdOn", new Date())
                        .set("modifiedOn", new Date()),
                CollectionMember.class);
    }

    public void delete(String collectionId, Affordance affordance, String organizationId) {
        DeleteResult deleteResult = mongoTemplate.remove(Query.query(Criteria.where("collectionId").is(collectionId)
                        .and("affordanceType").is(affordance.getType())
                        .and("affordanceId").is(affordance.getReferenceId())
                        .and("organizationId").is(organizationId)),
                CollectionMember.class);

        if (deleteResult.getDeletedCount() == 0) {
            throw new NotFoundException("Collection member not found");
        }
    }

    public List<AffordanceResponse> listAffordances(String collectionId, AffordanceType affordanceType, String organizationId, Pageable pageable) {
        List<CollectionMember> collectionMembers = collectionMemberRepository.findByCollectionIdAndAffordanceTypeAndOrganizationId(collectionId, affordanceType, organizationId, pageable);

        List<Affordance> affordances = new ArrayList<>();

        for (CollectionMember collectionMember : collectionMembers) {
            affordances.add(new Affordance(collectionMember.getAffordanceType(), collectionMember.getAffordanceId()));
        }

        return affordanceService.fetch(affordances);
    }

    public List<Collection> listCollections(Affordance affordance, String organizationId, Pageable pageable) {
        List<CollectionMember> collectionMembers = collectionMemberRepository.findByAffordanceTypeAndAffordanceIdAndOrganizationId(affordance.getType(),
                affordance.getReferenceId(), organizationId, pageable);

        List<String> collectionIds = collectionMembers.stream().map(CollectionMember::getCollectionId).toList();
        return collectionService.get(collectionIds);
    }

    public List<CollectionMember> listCollections(Affordance affordance) {
        return collectionMemberRepository.findByAffordanceTypeAndAffordanceId(affordance.getType(), affordance.getReferenceId());
    }
}
