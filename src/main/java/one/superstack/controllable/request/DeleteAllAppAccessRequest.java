package one.superstack.controllable.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import one.superstack.controllable.enums.TargetType;

import java.io.Serializable;

public class DeleteAllAppAccessRequest implements Serializable {

    @NotNull
    private TargetType targetType;

    @NotBlank
    private String targetId;

    public DeleteAllAppAccessRequest() {

    }

    public DeleteAllAppAccessRequest(TargetType targetType, String targetId) {
        this.targetType = targetType;
        this.targetId = targetId;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
