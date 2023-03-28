package one.superstack.controllable.service;

import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.model.AppAccess;
import one.superstack.controllable.repository.AppAccessRepository;
import one.superstack.controllable.request.DeleteAllAppAccessRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AppAccessGCService {

    private final MongoTemplate mongoTemplate;


    public AppAccessGCService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Async
    public void deleteAllForApp(String appId) {
        mongoTemplate.remove(Query.query(Criteria.where("appId").is(appId)), AppAccess.class);
    }

    @Async
    public void deleteAllForTarget(TargetType targetType, String targetId) {
        mongoTemplate.remove(Query.query(Criteria.where("targetType").is(targetType).and("targetId").is(targetId)), AppAccess.class);
    }
}
