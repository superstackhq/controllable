package one.superstack.controllable.service;

import one.superstack.controllable.model.Namespace;
import one.superstack.controllable.repository.NamespaceRepository;
import one.superstack.controllable.request.NamespaceFetchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NamespaceService {

    private final NamespaceRepository namespaceRepository;

    @Autowired
    public NamespaceService(NamespaceRepository namespaceRepository) {
        this.namespaceRepository = namespaceRepository;
    }

    public void register(List<String> namespace, String organizationId) {
        if (null == namespace || namespace.isEmpty()) {
            return;
        }

        List<String> parent = namespace.subList(0, namespace.size() - 1);
        String key = namespace.get(namespace.size() - 1);

        if (exists(parent, key, organizationId)) {
            return;
        }

        Namespace n = new Namespace(parent, key, organizationId);
        n = namespaceRepository.save(n);

        if (!parent.isEmpty()) {
            register(parent, organizationId);
        }
    }

    public List<Namespace> list(NamespaceFetchRequest namespaceFetchRequest, String organizationId, Pageable pageable) {
        return namespaceRepository.findByParentAndOrganizationId(namespaceFetchRequest.getParent(), organizationId, pageable);
    }

    public Boolean exists(List<String> parent, String key, String organizationId) {
        return namespaceRepository.existsByParentAndKeyAndOrganizationId(parent, key, organizationId);
    }
}
