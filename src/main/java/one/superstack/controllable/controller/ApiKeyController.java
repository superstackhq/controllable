package one.superstack.controllable.controller;

import jakarta.validation.Valid;
import one.superstack.controllable.auth.actor.AuthenticatedController;
import one.superstack.controllable.model.ApiKey;
import one.superstack.controllable.request.ApiKeyCreationRequest;
import one.superstack.controllable.request.ApiKeyUpdateRequest;
import one.superstack.controllable.request.FullAccessChangeRequest;
import one.superstack.controllable.response.AccessKeyResponse;
import one.superstack.controllable.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class ApiKeyController extends AuthenticatedController {

    private final ApiKeyService apiKeyService;

    @Autowired
    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping(value = "/api-keys")
    public ApiKey create(@Valid @RequestBody ApiKeyCreationRequest apiKeyCreationRequest) {
        checkFullAccess();
        return apiKeyService.create(apiKeyCreationRequest, getActor());
    }

    @GetMapping(value = "/api-keys")
    public List<ApiKey> list(Pageable pageable) {
        return apiKeyService.list(getOrganizationId(), pageable);
    }

    @GetMapping(value = "/api-keys/{apiKeyId}")
    public ApiKey get(@PathVariable String apiKeyId) throws Throwable {
        return apiKeyService.get(apiKeyId, getOrganizationId());
    }

    @PutMapping(value = "/api-keys/{apiKeyId}")
    public ApiKey update(@PathVariable String apiKeyId, @Valid @RequestBody ApiKeyUpdateRequest apiKeyUpdateRequest) throws Throwable {
        checkFullAccess();
        return apiKeyService.update(apiKeyId, apiKeyUpdateRequest, getOrganizationId());
    }

    @PutMapping(value = "/api-keys/{apiKeyId}/full-access")
    public ApiKey changeFullAccess(@PathVariable String apiKeyId, @Valid @RequestBody FullAccessChangeRequest fullAccessChangeRequest) throws Throwable {
        checkFullAccess();
        ;
        return apiKeyService.changeFullAccess(apiKeyId, fullAccessChangeRequest, getOrganizationId());
    }

    @PutMapping(value = "/api-keys/{apiKeyId}/access-key")
    public ApiKey resetAccessKey(@PathVariable String apiKeyId) throws Throwable {
        checkFullAccess();
        return apiKeyService.resetAccessKey(apiKeyId, getOrganizationId());
    }

    @GetMapping(value = "/api-keys/{apiKeyId}/access-key")
    public AccessKeyResponse getAccessKey(@PathVariable String apiKeyId) throws Throwable {
        checkFullAccess();
        return apiKeyService.getAccessKey(apiKeyId, getOrganizationId());
    }

    @DeleteMapping(value = "/api-keys/{apiKeyId}")
    public ApiKey delete(@PathVariable String apiKeyId) throws Throwable {
        checkFullAccess();
        return apiKeyService.delete(apiKeyId, getOrganizationId());
    }

    @GetMapping(value = "/api-keys/search")
    public List<ApiKey> search(@RequestParam String query, Pageable pageable) {
        return apiKeyService.search(query, getOrganizationId(), pageable);
    }
}
