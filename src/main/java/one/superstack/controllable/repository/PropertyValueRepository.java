package one.superstack.controllable.repository;

import one.superstack.controllable.embedded.Segment;
import one.superstack.controllable.model.PropertyValue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyValueRepository extends MongoRepository<PropertyValue, String> {

    Optional<PropertyValue> findByIdAndPropertyIdAndEnvironmentIdAndOrganizationId(String id, String propertyId, String environmentId, String organizationId);

    List<PropertyValue> findByPropertyIdAndEnvironmentIdAndOrganizationId(String propertyId, String environmentId, String organizationId, Pageable pageable);

    List<PropertyValue> findByPropertyIdAndEnvironmentIdAndSegmentAndOrganizationId(String propertyId, String environmentId, Segment segment, String organizationId,  Pageable pageable);

    List<PropertyValue> findByPropertyId(String propertyId);

    List<PropertyValue> findByEnvironmentId(String environmentId);
}
