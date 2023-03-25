package one.superstack.controllable.request;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class GroupMemberRequest implements Serializable {

    @NotBlank
    private String userId;

    public GroupMemberRequest() {
    }

    public GroupMemberRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
