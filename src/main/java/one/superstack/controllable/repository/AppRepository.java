package one.superstack.controllable.repository;

import one.superstack.controllable.model.App;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppRepository extends MongoRepository<App, String> {

    Optional<App> findByIdAndOrganizationId(String id, String organizationId);

    List<App> findByOrganizationId(String organizationId, Pageable pageable);

    List<App> findByIdIn(List<String> ids);

    Boolean existsByNameAndOrganizationId(String name, String organizationId);

    Boolean existsByIdNotAndNameAndOrganizationId(String id, String name, String organizationId);

    Boolean existsByIdAndOrganizationId(String id, String organizationId);
}
