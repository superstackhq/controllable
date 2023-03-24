package one.superstack.controllable.response;

import java.io.Serializable;

public class SuccessResponse implements Serializable {

    private Boolean success;

    private String message;

    public SuccessResponse() {

    }

    public SuccessResponse(String message) {
        this.success = true;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
