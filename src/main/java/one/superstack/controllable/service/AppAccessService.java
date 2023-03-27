package one.superstack.controllable.service;

import one.superstack.controllable.auth.AuthenticatedActor;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.App;
import one.superstack.controllable.model.AppAccess;
import one.superstack.controllable.pojo.Target;
import one.superstack.controllable.pojo.TargetReference;
import one.superstack.controllable.repository.AppAccessRepository;
import one.superstack.controllable.request.AppAccessRequest;
import one.superstack.controllable.request.DeleteAllAppAccessRequest;
import one.superstack.controllable.response.AppAccessResponse;
import one.superstack.controllable.response.TargetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class AppAccessService {

    private final AppAccessRepository appAccessRepository;

    private final AppService appService;

    private final TargetService targetService;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AppAccessService(AppAccessRepository appAccessRepository, AppService appService, TargetService targetService, MongoTemplate mongoTemplate) {
        this.appAccessRepository = appAccessRepository;
        this.appService = appService;
        this.targetService = targetService;
        this.mongoTemplate = mongoTemplate;
    }

    public void add(String appId, AppAccessRequest appAccessRequest, AuthenticatedActor creator) {
        if (!appService.exists(appId, creator.getOrganizationId())) {
            throw new ClientException("App not found");
        }

        if (!appAccessRequest.getTargetType().equals(TargetType.PROPERTY) && !appAccessRequest.getTargetType().equals(TargetType.COLLECTION)) {
            throw new ClientException("Invalid target type");
        }

        if (!targetService.exists(appAccessRequest.getTargetType(), appAccessRequest.getTargetId(), creator.getOrganizationId())) {
            throw new NotFoundException("Target not found");
        }

        mongoTemplate.upsert(Query.query(Criteria
                        .where("appId").is(appId)
                        .and("targetType").is(appAccessRequest.getTargetType())
                        .and("targetId").is(appAccessRequest.getTargetId())),
                new Update()
                        .setOnInsert("appId", appId)
                        .setOnInsert("targetType", appAccessRequest.getTargetType())
                        .setOnInsert("targetId", appAccessRequest.getTargetId())
                        .setOnInsert("creatorType", creator.getType())
                        .setOnInsert("creatorId", creator.getId())
                        .setOnInsert("organizationId", creator.getOrganizationId())
                        .set("modifiedOn", new Date())
                        .addToSet("permissions").each(appAccessRequest.getPermissions().toArray()),
                AppAccess.class);
    }

    public void delete(String appId, AppAccessRequest appAccessRequest, String organizationId) {
        mongoTemplate.updateFirst(Query.query(Criteria
                        .where("appId").is(appId)
                        .and("targetType").is(appAccessRequest.getTargetType())
                        .and("targetId").is(appAccessRequest.getTargetId())
                        .and("organizationId").is(organizationId)),
                new Update()
                        .pullAll("permissions", appAccessRequest.getPermissions().toArray())
                        .set("modifiedOn", new Date()),
                AppAccess.class);
    }

    public AppAccess get(String appId, TargetType targetType, String targetId, String organizationId) throws Throwable {
        return appAccessRepository.findByAppIdAndTargetTypeAndTargetIdAndOrganizationId(appId, targetType, targetId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("App access not found"));
    }

    public AppAccess deleteAll(String appId, DeleteAllAppAccessRequest deleteAllAppAccessRequest, String organizationId) throws Throwable {
        AppAccess appAccess = get(appId, deleteAllAppAccessRequest.getTargetType(), deleteAllAppAccessRequest.getTargetId(), organizationId);
        appAccessRepository.delete(appAccess);
        return appAccess;
    }

    @Async
    public void deleteAllForApp(String appId) {
        mongoTemplate.remove(Query.query(Criteria.where("appId").is(appId)), AppAccess.class);
    }

    @Async
    public void deleteAllForTarget(TargetType targetType, String targetId) {
        mongoTemplate.remove(Query.query(Criteria.where("targetType").is(targetType).and("targetId").is(targetId)), AppAccess.class);
    }

    public List<TargetResponse> listTargets(String appId, TargetType targetType, String organizationId, Pageable pageable) throws ExecutionException, InterruptedException {
        List<AppAccess> mappings = appAccessRepository.findByAppIdAndTargetTypeAndOrganizationId(appId, targetType, organizationId, pageable);

        List<TargetReference> targetReferences = new ArrayList<>();

        for (AppAccess appAccess : mappings) {
            targetReferences.add(new TargetReference(appAccess.getTargetType(), appAccess.getTargetId()));
        }

        Map<TargetReference, Target> targetMap = targetService.fetch(targetReferences)
                .stream().collect(Collectors.toMap(target -> new TargetReference(target.getType(), target.getReferenceId()), target -> target, (a, b) -> b));

        return mappings.stream()
                .map(appAccess -> new TargetResponse(targetMap.get(new TargetReference(appAccess.getTargetType(), appAccess.getTargetId())), appAccess.getPermissions())).toList();
    }

    public List<AppAccessResponse> listApps(TargetType targetType, String targetId, String organizationId, Pageable pageable) {
        List<AppAccess> mappings = appAccessRepository.findByTargetTypeAndTargetIdAndOrganizationId(targetType, targetId, organizationId, pageable);
        List<String> appIds = mappings.stream().map(AppAccess::getAppId).toList();
        Map<String, App> appMap = appService.get(appIds).stream().collect(Collectors.toMap(App::getId, app -> app, (a, b) -> b));

        List<AppAccessResponse> responses = new ArrayList<>();

        for (AppAccess appAccess : mappings) {
            responses.add(new AppAccessResponse(appMap.get(appAccess.getAppId()), appAccess.getPermissions()));
        }

        return responses;
    }
}
