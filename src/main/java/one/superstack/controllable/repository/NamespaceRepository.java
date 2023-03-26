package one.superstack.controllable.repository;

import one.superstack.controllable.model.Namespace;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NamespaceRepository extends MongoRepository<Namespace, String> {

    List<Namespace> findByParentAndOrganizationId(List<String> parent, String organizationId, Pageable pageable);

    Boolean existsByParentAndKeyAndOrganizationId(List<String> parent, String key, String organizationId);
}
