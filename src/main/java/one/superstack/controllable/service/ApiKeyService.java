package one.superstack.controllable.service;

import one.superstack.controllable.auth.AuthenticatedActor;
import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.exception.InvalidTokenException;
import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.ApiKey;
import one.superstack.controllable.repository.ApiKeyRepository;
import one.superstack.controllable.request.ApiKeyCreationRequest;
import one.superstack.controllable.request.ApiKeyUpdateRequest;
import one.superstack.controllable.request.FullAccessChangeRequest;
import one.superstack.controllable.response.AccessKeyResponse;
import one.superstack.controllable.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    private final AccessService accessService;

    @Autowired
    public ApiKeyService(ApiKeyRepository apiKeyRepository, AccessService accessService) {
        this.apiKeyRepository = apiKeyRepository;
        this.accessService = accessService;
    }

    public ApiKey create(ApiKeyCreationRequest apiKeyCreationRequest, AuthenticatedActor creator) {
        if (nameExists(apiKeyCreationRequest.getName(), creator.getOrganizationId())) {
            throw new ClientException("API key " + apiKeyCreationRequest.getName() + " already exists");
        }

        ApiKey apiKey = new ApiKey(apiKeyCreationRequest.getName(),
                apiKeyCreationRequest.getDescription(),
                Random.generateRandomString(128),
                apiKeyCreationRequest.getHasFullAccess(),
                creator.getOrganizationId(),
                creator.getType(),
                creator.getId());

        return apiKeyRepository.save(apiKey);
    }

    public List<ApiKey> list(String organizationId, Pageable pageable) {
        return apiKeyRepository.findByOrganizationId(organizationId, pageable);
    }

    @Async
    public CompletableFuture<List<ApiKey>> asyncGet(List<String> apiKeyIds) {
        return CompletableFuture.completedFuture(get(apiKeyIds));
    }

    public List<ApiKey> get(List<String> apiKeyIds) {
        return apiKeyRepository.findByIdIn(apiKeyIds);
    }

    public ApiKey get(String apiKeyId) throws Throwable {
        return apiKeyRepository.findById(apiKeyId).orElseThrow((Supplier<Throwable>) () -> new NotFoundException("API key not found"));
    }

    public ApiKey get(String apiKeyId, String organizationId) throws Throwable {
        return apiKeyRepository.findByIdAndOrganizationId(apiKeyId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("API key not found"));
    }

    public ApiKey update(String apiKeyId, ApiKeyUpdateRequest apiKeyUpdateRequest, String organizationId) throws Throwable {
        ApiKey apiKey = get(apiKeyId, organizationId);

        if (null != apiKeyUpdateRequest.getName() && !apiKeyUpdateRequest.getName().isBlank()) {
            if (apiKeyRepository.existsByIdNotAndNameAndOrganizationId(apiKeyId, apiKeyUpdateRequest.getName(), organizationId)) {
                throw new ClientException("API key " + apiKeyUpdateRequest.getName() + " already exists");
            }

            apiKey.setName(apiKeyUpdateRequest.getName());
        }

        apiKey.setDescription(apiKeyUpdateRequest.getDescription());
        apiKey.setModifiedOn(new Date());

        return apiKeyRepository.save(apiKey);
    }

    public ApiKey changeFullAccess(String apiKeyId, FullAccessChangeRequest fullAccessChangeRequest, String organizationId) throws Throwable {
        ApiKey apiKey = get(apiKeyId, organizationId);

        apiKey.setHasFullAccess(fullAccessChangeRequest.getHasFullAccess());
        apiKey.setModifiedOn(new Date());

        return apiKeyRepository.save(apiKey);
    }

    public ApiKey resetAccessKey(String apiKeyId, String organizationId) throws Throwable {
        ApiKey apiKey = get(apiKeyId, organizationId);

        apiKey.setAccessKey(Random.generateRandomString(128));
        apiKey.setModifiedOn(new Date());

        return apiKeyRepository.save(apiKey);
    }

    public AccessKeyResponse getAccessKey(String apiKeyId, String organizationId) throws Throwable {
        return new AccessKeyResponse(get(apiKeyId, organizationId).getAccessKey());
    }

    public ApiKey delete(String apiKeyId, String organizationId) throws Throwable {
        ApiKey apiKey = get(apiKeyId, organizationId);
        apiKeyRepository.delete(apiKey);
        accessService.deleteAllForActor(ActorType.API_KEY, apiKeyId);
        return apiKey;
    }

    public AuthenticatedActor getByAccessKey(String accessKey) throws Throwable {
        ApiKey apiKey = apiKeyRepository.findByAccessKey(accessKey)
                .orElseThrow((Supplier<Throwable>) InvalidTokenException::new);

        return new AuthenticatedActor(ActorType.API_KEY, apiKey.getId(), apiKey.getOrganizationId(), apiKey.getHasFullAccess());
    }

    public Boolean exists(String apiKeyId, String organizationId) {
        return apiKeyRepository.existsByIdAndOrganizationId(apiKeyId, organizationId);
    }

    public List<ApiKey> search(String  query, String organizationId, Pageable pageable) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(query);
        return apiKeyRepository.findByOrganizationIdOrderByScoreDesc(organizationId, textCriteria, pageable);
    }

    private Boolean nameExists(String name, String organizationId) {
        return apiKeyRepository.existsByNameAndOrganizationId(name, organizationId);
    }
}
