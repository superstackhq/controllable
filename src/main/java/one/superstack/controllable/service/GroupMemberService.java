package one.superstack.controllable.service;

import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.GroupMember;
import one.superstack.controllable.repository.GroupMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public GroupMemberService(GroupMemberRepository groupMemberRepository, MongoTemplate mongoTemplate) {
        this.groupMemberRepository = groupMemberRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public GroupMember save(String groupId, String userId, ActorType creatorType, String creatorId) {
        GroupMember groupMember = new GroupMember(groupId, userId, creatorType, creatorId);
        return groupMemberRepository.save(groupMember);
    }

    public GroupMember add(String groupId, String userId, ActorType creatorType, String creatorId) {
        if (isMember(groupId, userId)) {
            throw new ClientException("User is already added to this group");
        }

        return save(groupId, userId, creatorType, creatorId);
    }

    public GroupMember delete(String groupId, String userId) throws Throwable {
        GroupMember groupMember = groupMemberRepository.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("User is not added to this group"));
        groupMemberRepository.delete(groupMember);
        return groupMember;
    }

    public void deleteAllForGroup(String groupId) {
        mongoTemplate.remove(Query.query(Criteria.where("groupId").is(groupId)));
    }

    public void deleteAllForUser(String userId) {
        mongoTemplate.remove(Query.query(Criteria.where("userId").is(userId)));
    }

    public List<GroupMember> listGroups(String userId, Pageable pageable) {
        return groupMemberRepository.findByUserId(userId, pageable);
    }

    public List<GroupMember> listUsers(String groupId, Pageable pageable) {
        return groupMemberRepository.findByGroupId(groupId, pageable);
    }

    public Boolean isMember(String groupId, String userId) {
        return groupMemberRepository.existsByGroupIdAndUserId(groupId, userId);
    }
}
