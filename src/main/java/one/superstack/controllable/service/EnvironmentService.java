package one.superstack.controllable.service;

import one.superstack.controllable.auth.AuthenticatedActor;
import one.superstack.controllable.enums.Permission;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.Environment;
import one.superstack.controllable.repository.EnvironmentRepository;
import one.superstack.controllable.request.AccessRequest;
import one.superstack.controllable.request.EnvironmentCreationRequest;
import one.superstack.controllable.request.EnvironmentUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
public class EnvironmentService {

    private final EnvironmentRepository environmentRepository;

    private final AccessService accessService;

    private final PropertyValueGCService propertyValueGCService;

    @Autowired
    public EnvironmentService(EnvironmentRepository environmentRepository, AccessService accessService, PropertyValueGCService propertyValueGCService) {
        this.environmentRepository = environmentRepository;
        this.accessService = accessService;
        this.propertyValueGCService = propertyValueGCService;
    }

    public Environment create(EnvironmentCreationRequest environmentCreationRequest, AuthenticatedActor creator) {
        if (nameExists(environmentCreationRequest.getName(), creator.getOrganizationId())) {
            throw new ClientException("Environment " + environmentCreationRequest.getName() + " already exists");
        }

        Environment environment = new Environment(environmentCreationRequest.getName(),
                environmentCreationRequest.getDescription(),
                creator.getOrganizationId(),
                creator.getType(),
                creator.getId());

        environment = environmentRepository.save(environment);
        accessService.add(new AccessRequest(TargetType.ENVIRONMENT, environment.getId(), creator.getType(), creator.getId(), null, Set.of(Permission.ALL)), creator);
        return environment;
    }

    public List<Environment> list(String organizationId, Pageable pageable) {
        return environmentRepository.findByOrganizationId(organizationId, pageable);
    }

    @Async
    public CompletableFuture<List<Environment>> asyncGet(List<String> environmentIds) {
        return CompletableFuture.completedFuture(get(environmentIds));
    }

    public List<Environment> get(List<String> environmentIds) {
        return environmentRepository.findByIdIn(environmentIds);
    }

    public Environment get(String environmentId, String organizationId) throws Throwable {
        return environmentRepository.findByIdAndOrganizationId(environmentId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Environment not found"));
    }

    public Environment update(String environmentId, EnvironmentUpdateRequest environmentUpdateRequest, String organizationId) throws Throwable {
        Environment environment = get(environmentId, organizationId);

        if (null != environmentUpdateRequest.getName() && !environmentUpdateRequest.getName().isBlank()) {
            if (environmentRepository.existsByIdNotAndNameAndOrganizationId(environmentId, environmentUpdateRequest.getName(), organizationId)) {
                throw new ClientException("Environment " + environmentUpdateRequest.getName() + " already exists");
            }

            environment.setName(environmentUpdateRequest.getName());
        }

        environment.setDescription(environmentUpdateRequest.getDescription());
        environment.setModifiedOn(new Date());

        return environmentRepository.save(environment);
    }

    public Environment delete(String environmentId, AuthenticatedActor actor) throws Throwable {
        Environment environment = get(environmentId, actor.getOrganizationId());
        environmentRepository.delete(environment);
        accessService.deleteAllForEnvironment(environment.getId());
        accessService.deleteAllForTarget(TargetType.ENVIRONMENT, environmentId);
        propertyValueGCService.deleteAllForEnvironment(environmentId, actor);
        return environment;
    }

    public Boolean exists(String environmentId, String organizationId) {
        return environmentRepository.existsByIdAndOrganizationId(environmentId, organizationId);
    }

    private Boolean nameExists(String name, String organizationId) {
        return environmentRepository.existsByNameAndOrganizationId(name, organizationId);
    }
}
