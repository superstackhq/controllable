package one.superstack.controllable.service;

import one.superstack.controllable.enums.AffordanceType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.model.Property;
import one.superstack.controllable.pojo.Affordance;
import one.superstack.controllable.response.AffordanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AffordanceService {

    private final PropertyService propertyService;

    @Autowired
    public AffordanceService(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    public List<AffordanceResponse> fetch(List<Affordance> affordances) {
        List<String> propertyIds = new ArrayList<>();

        for (Affordance affordance : affordances) {
            if (AffordanceType.PROPERTY.equals(affordance.getType())) {
                propertyIds.add(affordance.getReferenceId());
            }
        }

        List<Property> properties = propertyService.get(propertyIds);

        List<AffordanceResponse> responses = new ArrayList<>();
        for (Property property : properties) {
            responses.add(new AffordanceResponse(AffordanceType.PROPERTY, property));
        }

        return responses;
    }

    public Boolean exists(Affordance affordance, String organizationId) {
        if (AffordanceType.PROPERTY.equals(affordance.getType())) {
            return propertyService.exists(affordance.getReferenceId(), organizationId);
        } else {
            throw new ClientException("Invalid affordance type");
        }
    }
}
