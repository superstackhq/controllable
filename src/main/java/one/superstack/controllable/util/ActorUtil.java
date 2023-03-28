package one.superstack.controllable.util;

import one.superstack.controllable.enums.ActorType;
import one.superstack.controllable.enums.PropertyActorType;
import one.superstack.controllable.exception.ClientException;

public class ActorUtil {

    public static PropertyActorType convert(ActorType actorType) {
        switch (actorType) {
            case API_KEY -> {
                return PropertyActorType.API_KEY;
            }
            case USER -> {
                return PropertyActorType.USER;
            }
            default -> throw new ClientException("Invalid actor type");
        }
    }
}
