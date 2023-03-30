package one.superstack.controllable.util;

public class StringUtil {

    public static Boolean isAlphaNumeric(String str) {
        return str.matches("[A-Za-z0-9]+");
    }
}
