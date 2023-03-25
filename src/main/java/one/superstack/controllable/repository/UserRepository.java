package one.superstack.controllable.repository;

import one.superstack.controllable.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsernameAndOrganizationId(String username, String organizationId);

    Optional<User> findByIdAndOrganizationId(String id, String organizationId);

    List<User> findByOrganizationId(String organizationId, Pageable pageable);

    List<User> findByIdIn(List<String> ids);

    Boolean existsByUsernameAndOrganizationId(String username, String organizationId);

    Boolean existsByIdAndOrganizationId(String id, String organizationId);
}
