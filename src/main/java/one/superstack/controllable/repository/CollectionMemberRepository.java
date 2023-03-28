package one.superstack.controllable.repository;

import one.superstack.controllable.enums.AffordanceType;
import one.superstack.controllable.model.CollectionMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionMemberRepository extends MongoRepository<CollectionMember, String> {

    List<CollectionMember> findByCollectionIdAndAffordanceTypeAndOrganizationId(String collectionId, AffordanceType affordanceType, String organizationId, Pageable pageable);

    List<CollectionMember> findByAffordanceTypeAndAffordanceIdAndOrganizationId(AffordanceType affordanceType, String affordanceId, String organizationId, Pageable pageable);

    List<CollectionMember> findByAffordanceTypeAndAffordanceId(AffordanceType affordanceType, String affordanceId);
}
