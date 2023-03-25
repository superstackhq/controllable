package one.superstack.controllable.repository;

import one.superstack.controllable.model.GroupMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends MongoRepository<GroupMember, String> {

    Optional<GroupMember> findByGroupIdAndUserId(String groupId, String userId);

    List<GroupMember> findByGroupId(String groupId, Pageable pageable);

    List<GroupMember> findByUserId(String userId, Pageable pageable);

    Boolean existsByGroupIdAndUserId(String groupId, String userId);
}
