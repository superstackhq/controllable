package one.superstack.controllable.embedded;

import java.io.Serializable;

public class Rule implements Serializable {

    private String expression;

    public Rule() {

    }

    public Rule(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
