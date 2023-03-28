package one.superstack.controllable.service;

import one.superstack.controllable.auth.AuthenticatedActor;
import one.superstack.controllable.enums.ChangeType;
import one.superstack.controllable.model.PropertyValue;
import one.superstack.controllable.model.PropertyValueLog;
import one.superstack.controllable.repository.PropertyValueRepository;
import one.superstack.controllable.util.ActorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        logDeletes(propertyValues, actor);
    }

    @Async
    public void deleteAllForEnvironment(String environmentId, AuthenticatedActor actor) {
        List<PropertyValue> propertyValues = propertyValueRepository.findByEnvironmentId(environmentId);
        propertyValueRepository.deleteAll(propertyValues);
        logDeletes(propertyValues, actor);
    }

    private void logDeletes(List<PropertyValue> propertyValues, AuthenticatedActor actor) {
        List<PropertyValueLog> logs = new ArrayList<>();

        for (PropertyValue propertyValue : propertyValues) {
            logs.add(new PropertyValueLog(ChangeType.DELETE,
                    null,
                    propertyValue.getId(),
                    propertyValue.getPropertyId(),
                    propertyValue.getEnvironmentId(),
                    propertyValue.getSegment(),
                    propertyValue.getRule(),
                    propertyValue.getValue(),
                    propertyValue.getOrganizationId(),
                    ActorUtil.convert(actor.getType()),
                    actor.getId()));
        }

        propertyValueLogService.log(logs);
    }
}
