package one.superstack.controllable.auth;

import java.io.Serializable;

public class AuthorizationHeader implements Serializable {

    private String type;

    private String content;

    public AuthorizationHeader() {

    }

    public AuthorizationHeader(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
