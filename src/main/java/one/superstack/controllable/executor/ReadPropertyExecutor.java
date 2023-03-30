package one.superstack.controllable.executor;

import one.superstack.controllable.auth.app.AuthenticatedApp;
import one.superstack.controllable.model.PropertyValue;
import one.superstack.controllable.pojo.AugmentedBulkPropertyExecutionRequest;
import one.superstack.controllable.response.BulkPropertyExecutionResponse;
import one.superstack.controllable.service.PropertyValueService;
import org.springframework.stereotype.Component;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;

@Component
public class ReadPropertyExecutor implements PropertyExecutor {

    private final PropertyValueService propertyValueService;

    private final ScriptEngine scriptEngine;


    public ReadPropertyExecutor(PropertyValueService propertyValueService) {
        this.propertyValueService = propertyValueService;
        this.scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
    }

    @Override
    public BulkPropertyExecutionResponse execute(AugmentedBulkPropertyExecutionRequest request, AuthenticatedApp app) {
        // Fetch property values

        // Construct the trees

        // Evaluate the trees
        return null;
    }

    private PropertyValue applyRules(List<PropertyValue> propertyValues, Map<String, Object> params) throws ScriptException {
        PropertyValue valueWithEmptyRule = null;
        PropertyValue valueWithPassingRule = null;

        for (PropertyValue propertyValue : propertyValues) {
            if (null == propertyValue.getRule() || null == propertyValue.getRule().getExpression() || propertyValue.getRule().getExpression().isBlank()) {
                if (null == valueWithEmptyRule) {
                    valueWithEmptyRule = propertyValue;
                }
            } else {
                Bindings bindings = scriptEngine.createBindings();
                bindings.putAll(params);
                Object result = scriptEngine.eval(propertyValue.getRule().getExpression(), bindings);

                if (result instanceof Boolean) {
                    if ((Boolean) result) {
                        valueWithPassingRule = propertyValue;
                        break;
                    }
                }
            }
        }

        if (null != valueWithPassingRule) {
            return valueWithEmptyRule;
        }

        return valueWithEmptyRule;
    }
}
