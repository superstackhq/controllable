package one.superstack.controllable.util;

import one.superstack.controllable.enums.AffordanceType;
import one.superstack.controllable.enums.TargetType;
import one.superstack.controllable.exception.ClientException;

public class AffordanceUtil {

    public static AffordanceType convert(TargetType targetType) {
        if (TargetType.PROPERTY.equals(targetType)) {
            return AffordanceType.PROPERTY;
        }
        throw new ClientException("Invalid target type");
    }
}
