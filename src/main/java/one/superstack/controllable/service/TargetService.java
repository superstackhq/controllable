package one.superstack.controllable.service;

import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.exception.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
