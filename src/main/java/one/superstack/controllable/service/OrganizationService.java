package one.superstack.controllable.service;

import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.Organization;
import one.superstack.controllable.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Organization create(String name, String creatorId) {
        Organization organization = new Organization(name, creatorId);
        return organizationRepository.save(organization);
    }

    public Organization get(String organizationId) throws Throwable {
        return organizationRepository.findById(organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Organization not found"));
    }

    public Organization getByName(String name) throws Throwable {
        return organizationRepository.findByName(name)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Organization " + name + " not found"));
    }

    public Boolean nameExists(String name) {
        return organizationRepository.existsByNameAndDeletedIsFalse(name);
    }
}
