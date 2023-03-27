package one.superstack.controllable.repository;

import one.superstack.controllable.model.Property;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends MongoRepository<Property, String> {

    Optional<Property> findByIdAndOrganizationId(String id, String organizationId);

    List<Property> findByOrganizationId(String organizationId, Pageable pageable);

    List<Property> findByNamespaceAndOrganizationId(List<String> namespace, String organizationId, Pageable pageable);

    Boolean existsByNamespaceAndKeyAndVersionAndOrganizationId(List<String> namespace, String key, String version, String organizationId);

    Boolean existsByIdAndOrganizationId(String id, String organizationId);
}
