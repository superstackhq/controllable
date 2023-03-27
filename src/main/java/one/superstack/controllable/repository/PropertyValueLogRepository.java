package one.superstack.controllable.repository;

import one.superstack.controllable.model.PropertyValueLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyValueLogRepository extends MongoRepository<PropertyValueLog, String> {

    Optional<PropertyValueLog> findByIdAndPropertyValueIdAndPropertyIdAndEnvironmentIdAndOrganizationId(String id, String propertyValueId, String propertyId, String environmentId, String organizationId);

    List<PropertyValueLog> findByPropertyValueIdAndPropertyIdAndEnvironmentIdAndOrganizationId(String propertyValueId, String propertyId, String environmentId, String organizationId, Pageable pageable);
}
