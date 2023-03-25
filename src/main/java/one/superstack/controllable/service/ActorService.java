package one.superstack.controllable.service;

import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.exception.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActorService {

    private final UserService userService;

    private final ApiKeyService apiKeyService;

    private final GroupService groupService;

    @Autowired
    public ActorService(UserService userService, ApiKeyService apiKeyService, GroupService groupService) {
        this.userService = userService;
        this.apiKeyService = apiKeyService;
        this.groupService = groupService;
    }

    public Boolean exists(ActorType actorType, String actorId, String organizationId) {
        switch (actorType) {
            case USER -> {
                return userService.exists(actorId, organizationId);
            }

            case GROUP -> {
                return groupService.exists(actorId, organizationId);
            }

            case API_KEY -> {
                return apiKeyService.exists(actorId, organizationId);
            }

            default -> throw new ClientException("Invalid actor type");
        }
    }
}
