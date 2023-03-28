package one.superstack.controllable.util;

import one.superstack.controllable.enums.DataType;
import one.superstack.controllable.exception.ClientException;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DataTypeValidator {

    public static Boolean validate(DataType dataType, Object data) {
        if (data == null) {
            return false;
        }

        switch (dataType) {

            case OBJECT -> {
                return data instanceof LinkedHashMap<?, ?>;
            }
            case ARRAY -> {
                return data instanceof ArrayList<?>;
            }
            case STRING -> {
                return data instanceof String;
            }
            case INTEGER -> {
                return data instanceof Integer;
            }
            case FLOAT -> {
                return data instanceof Double;
            }
            case BOOLEAN -> {
                return data instanceof Boolean;
            }
            default -> throw new ClientException("Invalid data type");
        }
    }
}
