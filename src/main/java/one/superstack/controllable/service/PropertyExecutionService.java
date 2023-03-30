package one.superstack.controllable.service;

import one.superstack.controllable.auth.app.AuthenticatedApp;
import one.superstack.controllable.enums.ExecutionOperation;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.exception.NotAllowedException;
import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.executor.*;
import one.superstack.controllable.model.Environment;
import one.superstack.controllable.model.Property;
import one.superstack.controllable.pojo.AugmentedBulkPropertyExecutionRequest;
import one.superstack.controllable.pojo.AugmentedProperty;
import one.superstack.controllable.pojo.AugmentedPropertyExecutionRequest;
import one.superstack.controllable.pojo.PropertyReference;
import one.superstack.controllable.request.BulkPropertyExecutionRequest;
import one.superstack.controllable.request.PropertyExecutionRequest;
import one.superstack.controllable.response.BulkPropertyExecutionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PropertyExecutionService {

    private final Map<ExecutionOperation, PropertyExecutor> propertyExecutorMap;

    private final EnvironmentService environmentService;

    private final PropertyService propertyService;

    private final ExecutionAccessService executionAccessService;

    @Autowired
    public PropertyExecutionService(CreatePropertyExecutor createPropertyExecutor,
                                    ReadPropertyExecutor readPropertyExecutor,
                                    UpdatePropertyExecutor updatePropertyExecutor,
                                    DeletePropertyExecutor deletePropertyExecutor,
                                    EnvironmentService environmentService,
                                    PropertyService propertyService,
                                    ExecutionAccessService executionAccessService) {
        this.propertyExecutorMap = Map.of(
                ExecutionOperation.CREATE_PROPERTY_VALUE, createPropertyExecutor,
                ExecutionOperation.READ_PROPERTY_VALUE, readPropertyExecutor,
                ExecutionOperation.UPDATE_PROPERTY_VALUE, updatePropertyExecutor,
                ExecutionOperation.DELETE_PROPERTY_VALUE, deletePropertyExecutor
        );

        this.environmentService = environmentService;
        this.propertyService = propertyService;
        this.executionAccessService = executionAccessService;
    }

    public BulkPropertyExecutionResponse execute(BulkPropertyExecutionRequest request, AuthenticatedApp app) throws Throwable {
        // Fetch the executor
        PropertyExecutor executor = propertyExecutorMap.get(request.getOperation());

        if (null == executor) {
            throw new ClientException("Invalid operation type");
        }

        // Fetch the environment
        Environment environment = environmentService.getByName(request.getEnvironment(), app.getOrganizationId());

        // Augment the request
        AugmentedBulkPropertyExecutionRequest augmentedBulkPropertyExecutionRequest = augmentRequest(request, environment, app);

        // Check access
        if (!executionAccessService.hasPermission(augmentedBulkPropertyExecutionRequest.getPropertyIds(),
                app.getId(), getPermissionForOperation(request.getOperation()))) {
            throw new NotAllowedException();
        }

        // Execute
        return executor.execute(augmentedBulkPropertyExecutionRequest, app);
    }

    private AugmentedBulkPropertyExecutionRequest augmentRequest(BulkPropertyExecutionRequest bulkPropertyExecutionRequest, Environment environment, AuthenticatedApp app) {
        Set<PropertyReference> propertyReferences = bulkPropertyExecutionRequest.getRequests().stream().map(request -> new PropertyReference(request.getProperty().getNamespace(),
                request.getProperty().getKey(),
                request.getProperty().getVersion())).collect(Collectors.toSet());

        List<Property> properties = propertyService.getByReferences(propertyReferences, app.getOrganizationId());

        if (properties.size() != propertyReferences.size()) {
            throw new NotFoundException("All properties not found");
        }

        Map<PropertyReference, Property> propertyMap = new HashMap<>();
        Set<String> propertyIds = new HashSet<>();

        for (Property property : properties) {
            propertyIds.add(property.getId());
            propertyMap.put(property.toReference(), property);
        }

        List<AugmentedPropertyExecutionRequest> augmentedRequests = new ArrayList<>();

        for (PropertyExecutionRequest propertyExecutionRequest : bulkPropertyExecutionRequest.getRequests()) {
            PropertyReference propertyReference = propertyExecutionRequest.getProperty();
            AugmentedProperty augmentedProperty = new AugmentedProperty(propertyReference, propertyMap.get(propertyReference));
            augmentedRequests.add(new AugmentedPropertyExecutionRequest(augmentedProperty, propertyExecutionRequest.getValue(), propertyExecutionRequest.getParams()));
        }

        AugmentedBulkPropertyExecutionRequest augmentedBulkPropertyExecutionRequest = new AugmentedBulkPropertyExecutionRequest();
        augmentedBulkPropertyExecutionRequest.setEnvironment(environment);
        augmentedBulkPropertyExecutionRequest.setOperation(bulkPropertyExecutionRequest.getOperation());
        augmentedBulkPropertyExecutionRequest.setPropertyIds(propertyIds);
        augmentedBulkPropertyExecutionRequest.setRequests(augmentedRequests);
        return augmentedBulkPropertyExecutionRequest;
    }

    private Permission getPermissionForOperation(ExecutionOperation operation) {
        switch (operation) {

            case CREATE_PROPERTY_VALUE -> {
                return Permission.CREATE_PROPERTY_VALUE;
            }

            case READ_PROPERTY_VALUE -> {
                return Permission.READ_PROPERTY_VALUE;
            }

            case UPDATE_PROPERTY_VALUE -> {
                return Permission.UPDATE_PROPERTY_VALUE;
            }

            case DELETE_PROPERTY_VALUE -> {
                return Permission.DELETE_PROPERTY_VALUE;
            }

            default -> throw new ClientException("Invalid execution operation");
        }
    }
}
