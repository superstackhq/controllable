package one.superstack.controllable.repository;

import one.superstack.controllable.model.ApiKey;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiKeyRepository extends MongoRepository<ApiKey, String> {

    Optional<ApiKey> findByAccessKey(String accessKey);

    Optional<ApiKey> findByIdAndOrganizationId(String id, String organizationId);

    List<ApiKey> findByOrganizationId(String organizationId, Pageable pageable);

    List<ApiKey> findByIdIn(List<String> ids);

    Boolean existsByNameAndOrganizationId(String name, String organizationId);

    Boolean existsByIdNotAndNameAndOrganizationId(String id, String name, String organizationId);

    Boolean existsByIdAndOrganizationId(String id, String organizationId);

    List<ApiKey> findByOrganizationIdOrderByScoreDesc(String organizationId, TextCriteria textCriteria, Pageable pageable);
}
