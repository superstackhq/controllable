package one.superstack.controllable.service;

import one.superstack.controllable.auth.actor.AuthenticatedActor;
import one.superstack.controllable.model.PropertyValue;
import one.superstack.controllable.repository.PropertyValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyValueGCService {

    private final PropertyValueRepository propertyValueRepository;

    private final PropertyValueLogService propertyValueLogService;

    @Autowired
    public PropertyValueGCService(PropertyValueRepository propertyValueRepository, PropertyValueLogService propertyValueLogService) {
        this.propertyValueRepository = propertyValueRepository;
        this.propertyValueLogService = propertyValueLogService;
    }

    @Async
    public void deleteAllForProperty(String propertyId, AuthenticatedActor actor) {
        List<PropertyValue> propertyValues = propertyValueRepository.findByPropertyId(propertyId);
        propertyValueRepository.deleteAll(propertyValues);
        propertyValueLogService.logDeletes(propertyValues, actor);
    }

    @Async
    public void deleteAllForEnvironment(String environmentId, AuthenticatedActor actor) {
        List<PropertyValue> propertyValues = propertyValueRepository.findByEnvironmentId(environmentId);
        propertyValueRepository.deleteAll(propertyValues);
        propertyValueLogService.logDeletes(propertyValues, actor);
    }
}
