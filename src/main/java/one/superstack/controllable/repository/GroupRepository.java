package one.superstack.controllable.repository;

import one.superstack.controllable.model.Group;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {

    Optional<Group> findByIdAndOrganizationId(String id, String organizationId);

    List<Group> findByOrganizationId(String organizationId, Pageable pageable);

    List<Group> findByIdIn(List<String> ids);

    Boolean existsByNameAndOrganizationId(String name, String organizationId);

    Boolean existsByIdAndOrganizationId(String id, String organizationId);

    Boolean existsByIdNotAndNameAndOrganizationId(String id, String name, String organizationId);
}
