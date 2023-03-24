package one.superstack.controllable.repository;

import one.superstack.controllable.model.ApiKey;
import one.superstack.controllable.service.ApiKeyService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiKeyRepository extends MongoRepository<ApiKey, String> {

    Optional<ApiKey> findByAccessKey(String accessKey);

    Optional<ApiKey> findByIdAndOrganizationId(String id, String organizationId);

    List<ApiKey> findByOrganizationId(String organizationId, Pageable pageable);

    Boolean existsByNameAndOrganizationIdAndDeletedIsFalse(String name, String organizationId);

    Boolean existsByIdNotAndNameAndOrganizationIdAndDeletedIsFalse(String id, String name, String organizationId);
}
