package one.superstack.controllable.repository;

import one.superstack.controllable.model.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnvironmentRepository extends MongoRepository<Environment, String> {

    Optional<Environment> findByIdAndOrganizationId(String id, String organizationId);

    List<Environment> findByOrganizationId(String organizationId, Pageable pageable);

    Boolean existsByIdAndOrganizationId(String id, String organizationId);

    Boolean existsByNameAndOrganizationId(String name, String organizationId);

    Boolean existsByIdNotAndNameAndOrganizationId(String id, String name, String organizationId);
}
