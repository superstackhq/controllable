package one.superstack.controllable.request;

import java.io.Serializable;

public class PropertyValueDeletionRequest implements Serializable {

    private String changeMessage;

    public PropertyValueDeletionRequest() {

    }

    public PropertyValueDeletionRequest(String changeMessage) {
        this.changeMessage = changeMessage;
    }

    public String getChangeMessage() {
        return changeMessage;
    }

    public void setChangeMessage(String changeMessage) {
        this.changeMessage = changeMessage;
    }
}
