package one.superstack.controllable.service;

import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.model.App;
import one.superstack.controllable.model.Collection;
import one.superstack.controllable.model.Environment;
import one.superstack.controllable.model.Property;
import one.superstack.controllable.pojo.Target;
import one.superstack.controllable.pojo.TargetReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class TargetService {

    private final AppService appService;

    private final EnvironmentService environmentService;

    private final CollectionService collectionService;

    private final PropertyService propertyService;

    @Autowired
    public TargetService(AppService appService, EnvironmentService environmentService, CollectionService collectionService, PropertyService propertyService) {
        this.appService = appService;
        this.environmentService = environmentService;
        this.collectionService = collectionService;
        this.propertyService = propertyService;
    }

    public Boolean exists(TargetType targetType, String targetId, String organizationId) {
        switch (targetType) {
            case APP -> {
                return appService.exists(targetId, organizationId);
            }

            case PROPERTY -> {
                return propertyService.exists(targetId, organizationId);
            }

            case COLLECTION -> {
                return collectionService.exists(targetId, organizationId);
            }

            case ENVIRONMENT -> {
                return environmentService.exists(targetId, organizationId);
            }

            default -> throw new ClientException("Invalid target type");
        }
    }

    public List<Target> fetch(List<TargetReference> targetReferences) throws ExecutionException, InterruptedException {
        List<String> propertyIds = new ArrayList<>();
        List<String> appIds = new ArrayList<>();
        List<String> collectionIds = new ArrayList<>();
        List<String> environmentIds = new ArrayList<>();

        for (TargetReference targetReference : targetReferences) {
            switch (targetReference.getType()) {
                case PROPERTY -> propertyIds.add(targetReference.getId());
                case APP -> appIds.add(targetReference.getId());
                case COLLECTION -> collectionIds.add(targetReference.getId());
                case ENVIRONMENT -> environmentIds.add(targetReference.getId());
            }
        }

        CompletableFuture<List<Property>> propertiesFuture = null;
        if (!propertyIds.isEmpty()) {
            propertiesFuture = propertyService.asyncGet(propertyIds);
        }

        CompletableFuture<List<App>> appsFuture = null;
        if (!appIds.isEmpty()) {
            appsFuture = appService.asyncGet(appIds);
        }

        CompletableFuture<List<Collection>> collectionsFuture = null;
        if (!collectionIds.isEmpty()) {
            collectionsFuture = collectionService.asyncGet(collectionIds);
        }

        CompletableFuture<List<Environment>> environmentsFuture = null;
        if (!environmentIds.isEmpty()) {
            environmentsFuture = environmentService.asyncGet(environmentIds);
        }

        List<Target> responses = new ArrayList<>();

        if (null != propertiesFuture) {
            for (Property property : propertiesFuture.get()) {
                responses.add(new Target(TargetType.PROPERTY, property.getId(), property));
            }
        }

        if (null != appsFuture) {
            for (App app : appsFuture.get()) {
                responses.add(new Target(TargetType.APP, app.getId(), app));
            }
        }

        if (null != collectionsFuture) {
            for (Collection collection : collectionsFuture.get()) {
                responses.add(new Target(TargetType.COLLECTION, collection.getId(), collection));
            }
        }

        if (null != environmentsFuture) {
            for (Environment environment : environmentsFuture.get()) {
                responses.add(new Target(TargetType.ENVIRONMENT, environment.getId(), environment));
            }
        }

        return responses;
    }
}
