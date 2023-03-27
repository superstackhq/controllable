package one.superstack.controllable.service;

import one.superstack.controllable.enums.PropertyActorType;
import one.superstack.controllable.model.ApiKey;
import one.superstack.controllable.model.App;
import one.superstack.controllable.model.User;
import one.superstack.controllable.pojo.PropertyActor;
import one.superstack.controllable.pojo.PropertyActorReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class PropertyActorService {

    private final UserService userService;

    private final AppService appService;

    private final ApiKeyService apiKeyService;

    @Autowired
    public PropertyActorService(UserService userService, AppService appService, ApiKeyService apiKeyService) {
        this.userService = userService;
        this.appService = appService;
        this.apiKeyService = apiKeyService;
    }

    public List<PropertyActor> fetch(List<PropertyActorReference> propertyActorReferences) throws ExecutionException, InterruptedException {
        List<String> userIds = new ArrayList<>();
        List<String> appIds = new ArrayList<>();
        List<String> apiKeyIds = new ArrayList<>();

        for (PropertyActorReference propertyActorReference : propertyActorReferences) {
            switch (propertyActorReference.getType()) {
                case USER -> userIds.add(propertyActorReference.getId());
                case APP -> appIds.add(propertyActorReference.getId());
                case API_KEY -> apiKeyIds.add(propertyActorReference.getId());
            }
        }

        CompletableFuture<List<User>> usersFuture = null;
        CompletableFuture<List<App>> appsFuture = null;
        CompletableFuture<List<ApiKey>> apiKeysFuture = null;

        if (!userIds.isEmpty()) {
            usersFuture = userService.asyncGet(userIds);
        }

        if (!appIds.isEmpty()) {
            appsFuture = appService.asyncGet(appIds);
        }

        if (!apiKeyIds.isEmpty()) {
            apiKeysFuture = apiKeyService.asyncGet(apiKeyIds);
        }

        List<PropertyActor> propertyActors = new ArrayList<>();

        if (null != usersFuture) {
            for (User user : usersFuture.get()) {
                propertyActors.add(new PropertyActor(PropertyActorType.USER, user.getId(), user));
            }
        }

        if (null != appsFuture) {
            for (App app : appsFuture.get()) {
                propertyActors.add(new PropertyActor(PropertyActorType.APP, app.getId(), app));
            }
        }

        if (null != apiKeysFuture) {
            for (ApiKey apiKey : apiKeysFuture.get()) {
                propertyActors.add(new PropertyActor(PropertyActorType.API_KEY, apiKey.getId(), apiKey));
            }
        }

        return propertyActors;
    }
}
