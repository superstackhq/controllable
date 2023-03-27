package one.superstack.controllable.repository;

import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.model.AppAccess;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppAccessRepository extends MongoRepository<AppAccess, String> {

    Optional<AppAccess> findByAppIdAndTargetTypeAndTargetIdAndOrganizationId(String appId, TargetType targetType, String targetId, String organizationId);

    List<AppAccess> findByAppIdAndTargetTypeAndOrganizationId(String appId, TargetType targetType, String organizationId, Pageable pageable);

    List<AppAccess> findByTargetTypeAndTargetIdAndOrganizationId(TargetType targetType, String targetId, String organizationId, Pageable pageable);
}
