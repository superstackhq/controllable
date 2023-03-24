package one.superstack.controllable.exception;

public class NotAllowedException extends RuntimeException {

    @Override
    public String getMessage() {
        return "You are not allowed to perform this action";
    }
}