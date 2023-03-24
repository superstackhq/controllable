package one.superstack.controllable.response;

import java.io.Serializable;

public class AccessKeyResponse implements Serializable {

    private String accessKey;

    public AccessKeyResponse() {

    }

    public AccessKeyResponse(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
}
