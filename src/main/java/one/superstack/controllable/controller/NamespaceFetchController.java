package one.superstack.controllable.controller;

import jakarta.validation.Valid;
import one.superstack.controllable.auth.actor.AuthenticatedController;
import one.superstack.controllable.model.Namespace;
import one.superstack.controllable.request.NamespaceFetchRequest;
import one.superstack.controllable.service.NamespaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class NamespaceFetchController extends AuthenticatedController {

    private final NamespaceService namespaceService;

    @Autowired
    public NamespaceFetchController(NamespaceService namespaceService) {
        this.namespaceService = namespaceService;
    }

    @PostMapping(value = "/namespaces/fetch")
    public List<Namespace> fetch(@Valid @RequestBody NamespaceFetchRequest namespaceFetchRequest, Pageable pageable) {
        return namespaceService.list(namespaceFetchRequest, getOrganizationId(), pageable);
    }
}
