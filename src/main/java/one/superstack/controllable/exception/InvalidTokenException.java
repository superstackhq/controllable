package one.superstack.controllable.exception;

public class InvalidTokenException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Invalid access token";
    }
}