package one.superstack.controllable.service;

import one.superstack.controllable.auth.AuthenticatedActor;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.App;
import one.superstack.controllable.repository.AppRepository;
import one.superstack.controllable.request.AccessRequest;
import one.superstack.controllable.request.AppCreationRequest;
import one.superstack.controllable.request.AppUpdateRequest;
import one.superstack.controllable.response.AccessKeyResponse;
import one.superstack.controllable.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
public class AppService {

    private final AppRepository appRepository;

    private final AccessService accessService;

    private final AppAccessGCService appAccessGCService;

    @Autowired
    public AppService(AppRepository appRepository, AccessService accessService, AppAccessGCService appAccessGCService) {
        this.appRepository = appRepository;
        this.accessService = accessService;
        this.appAccessGCService = appAccessGCService;
    }

    public App create(AppCreationRequest appCreationRequest, AuthenticatedActor creator) {
        if (nameExists(appCreationRequest.getName(), creator.getOrganizationId())) {
            throw new ClientException("App " + appCreationRequest.getName() + " already exists");
        }

        App app = new App(appCreationRequest.getName(),
                appCreationRequest.getDescription(),
                Random.generateRandomString(128),
                creator.getOrganizationId(),
                creator.getType(),
                creator.getId());


        app = appRepository.save(app);
        accessService.add(new AccessRequest(TargetType.APP, app.getId(), creator.getType(), creator.getId(), null, Set.of(Permission.ALL)), creator);

        return app;
    }

    public List<App> list(String organizationId, Pageable pageable) {
        return appRepository.findByOrganizationId(organizationId, pageable);
    }

    @Async
    public CompletableFuture<List<App>> asyncGet(List<String> appIds) {
        return CompletableFuture.completedFuture(get(appIds));
    }

    public List<App> get(List<String> appIds) {
        return appRepository.findByIdIn(appIds);
    }

    public App get(String appId, String organizationId) throws Throwable {
        return appRepository.findByIdAndOrganizationId(appId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("App not found"));
    }

    public App update(String appId, AppUpdateRequest appUpdateRequest, String organizationId) throws Throwable {
        App app = get(appId, organizationId);

        if (null != appUpdateRequest.getName() && !appUpdateRequest.getName().isBlank()) {
            if (appRepository.existsByIdNotAndNameAndOrganizationId(appId, appUpdateRequest.getName(), organizationId)) {
                throw new ClientException("App " + appUpdateRequest.getName() + " already exists");
            }

            app.setName(appUpdateRequest.getName());
        }

        app.setDescription(appUpdateRequest.getDescription());

        app.setModifiedOn(new Date());
        return appRepository.save(app);
    }

    public App delete(String appId, String organizationId) throws Throwable {
        App app = get(appId, organizationId);
        appRepository.delete(app);
        accessService.deleteAllForTarget(TargetType.APP, appId);
        appAccessGCService.deleteAllForApp(appId);
        return app;
    }

    public AccessKeyResponse getAccessKey(String appId, String organizationId) throws Throwable {
        return new AccessKeyResponse(get(appId, organizationId).getAccessKey());
    }

    public App resetAccessKey(String appId, String organizationId) throws Throwable {
        App app = get(appId, organizationId);

        app.setAccessKey(Random.generateRandomString(128));
        app.setModifiedOn(new Date());

        return appRepository.save(app);
    }

    public Boolean exists(String appId, String organizationId) {
        return appRepository.existsByIdAndOrganizationId(appId, organizationId);
    }

    public List<App> search(String query, String organizationId, Pageable pageable) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(query);
        return appRepository.findByOrganizationIdOrderByScoreDesc(organizationId, textCriteria, pageable);
    }

    private Boolean nameExists(String name, String organizationId) {
        return appRepository.existsByNameAndOrganizationId(name, organizationId);
    }
}
