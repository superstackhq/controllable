package one.superstack.controllable.pojo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import one.superstack.controllable.enums.AffordanceType;

import java.io.Serializable;

public class Affordance implements Serializable {

    @NotNull
    private AffordanceType type;

    @NotBlank
    private String referenceId;

    public Affordance() {

    }

    public Affordance(AffordanceType type, String referenceId) {
        this.type = type;
        this.referenceId = referenceId;
    }

    public AffordanceType getType() {
        return type;
    }

    public void setType(AffordanceType type) {
        this.type = type;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
