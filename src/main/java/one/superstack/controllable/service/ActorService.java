package one.superstack.controllable.service;

import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.model.ApiKey;
import one.superstack.controllable.model.Group;
import one.superstack.controllable.model.User;
import one.superstack.controllable.pojo.Actor;
import one.superstack.controllable.pojo.ActorReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    public List<Actor> fetch(List<ActorReference> actorReferences) throws ExecutionException, InterruptedException {
        List<String> userIds = new ArrayList<>();
        List<String> groupIds = new ArrayList<>();
        List<String> apiKeyIds = new ArrayList<>();

        for (ActorReference actorReference : actorReferences) {
            switch (actorReference.getType()) {
                case USER -> userIds.add(actorReference.getId());
                case GROUP -> groupIds.add(actorReference.getId());
                case API_KEY -> apiKeyIds.add(actorReference.getId());
            }
        }

        CompletableFuture<List<User>> usersFuture = null;
        CompletableFuture<List<Group>> groupsFuture = null;
        CompletableFuture<List<ApiKey>> apiKeysFuture = null;

        if (!userIds.isEmpty()) {
            usersFuture = userService.asyncGet(userIds);
        }

        if (!groupIds.isEmpty()) {
            groupsFuture = groupService.asyncGet(groupIds);
        }

        if (!apiKeyIds.isEmpty()) {
            apiKeysFuture = apiKeyService.asyncGet(apiKeyIds);
        }

        List<Actor> actors = new ArrayList<>();

        if (null != usersFuture) {
            for (User user : usersFuture.get()) {
                actors.add(new Actor(ActorType.USER, user.getId(), user));
            }
        }

        if (null != groupsFuture) {
            for (Group group : groupsFuture.get()) {
                actors.add(new Actor(ActorType.GROUP, group.getId(), group));
            }
        }

        if (null != apiKeysFuture) {
            for (ApiKey apiKey : apiKeysFuture.get()) {
                actors.add(new Actor(ActorType.API_KEY, apiKey.getId(), apiKey));
            }
        }

        return actors;
    }

    public Actor get(ActorReference actorReference) throws Throwable {
        switch (actorReference.getType()) {
            case USER -> {
                return new Actor(ActorType.USER, actorReference.getId(), userService.get(actorReference.getId()));
            }

            case GROUP -> {
                return new Actor(ActorType.GROUP, actorReference.getId(), groupService.get(actorReference.getId()));
            }

            case API_KEY -> {
                return new Actor(ActorType.API_KEY, actorReference.getId(), apiKeyService.get(actorReference.getId()));
            }

            default -> throw new ClientException("Invalid actor type");
        }
    }
}
