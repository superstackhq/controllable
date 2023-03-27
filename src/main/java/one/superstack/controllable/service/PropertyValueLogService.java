package one.superstack.controllable.service;

import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.PropertyValueLog;
import one.superstack.controllable.pojo.PropertyActor;
import one.superstack.controllable.pojo.PropertyActorReference;
import one.superstack.controllable.repository.PropertyValueLogRepository;
import one.superstack.controllable.response.PropertyValueLogResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class PropertyValueLogService {

    private final PropertyValueLogRepository propertyValueLogRepository;

    private final PropertyActorService propertyActorService;

    @Autowired
    public PropertyValueLogService(PropertyValueLogRepository propertyValueLogRepository, PropertyActorService propertyActorService) {
        this.propertyValueLogRepository = propertyValueLogRepository;
        this.propertyActorService = propertyActorService;
    }

    public void log(List<PropertyValueLog> logs) {
        propertyValueLogRepository.saveAll(logs);
    }

    public void log(PropertyValueLog log) {
        propertyValueLogRepository.save(log);
    }

    public List<PropertyValueLogResponse> list(String propertyValueId, String propertyId, String environmentId, String organizationId, Pageable pageable) throws ExecutionException, InterruptedException {
        List<PropertyValueLog> propertyValueLogs = propertyValueLogRepository.findByPropertyValueIdAndPropertyIdAndEnvironmentIdAndOrganizationId(propertyValueId,
                propertyId, environmentId, organizationId, pageable);

        List<PropertyActorReference> propertyActorReferences = propertyValueLogs.stream().map(propertyValueLog -> new PropertyActorReference(propertyValueLog.getCreatorType(), propertyValueLog.getCreatorId())).collect(Collectors.toList());
        List<PropertyActor> propertyActors = propertyActorService.fetch(propertyActorReferences);
        Map<PropertyActorReference, PropertyActor> propertyActorMap = propertyActors.stream().collect(Collectors.toMap(propertyActor -> new PropertyActorReference(propertyActor.getType(), propertyActor.getReferenceId()), propertyActor -> propertyActor, (a, b) -> b));

        List<PropertyValueLogResponse> responses = new ArrayList<>();

        for (PropertyValueLog propertyValueLog : propertyValueLogs) {
            responses.add(new PropertyValueLogResponse(propertyValueLog, propertyActorMap.get(new PropertyActorReference(propertyValueLog.getCreatorType(), propertyValueLog.getCreatorId()))));
        }

        return responses;
    }

    public PropertyValueLog get(String propertyValueLogId, String propertyValueId, String propertyId, String environmentId, String organizationId) throws Throwable {
        return propertyValueLogRepository.findByIdAndPropertyValueIdAndPropertyIdAndEnvironmentIdAndOrganizationId(propertyValueLogId,
                propertyValueId, propertyId, environmentId, organizationId).orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Property value log not found"));
    }
}