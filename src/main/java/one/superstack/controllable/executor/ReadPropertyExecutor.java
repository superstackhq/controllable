package one.superstack.controllable.executor;

import one.superstack.controllable.auth.app.AuthenticatedApp;
import one.superstack.controllable.embedded.SegmentTreeLevel;
import one.superstack.controllable.model.Property;
import one.superstack.controllable.model.PropertyValue;
import one.superstack.controllable.pojo.AugmentedBulkPropertyExecutionRequest;
import one.superstack.controllable.pojo.AugmentedPropertyExecutionRequest;
import one.superstack.controllable.pojo.PropertyValueTreeNode;
import one.superstack.controllable.pojo.PropertyValueTrees;
import one.superstack.controllable.response.BulkPropertyExecutionResponse;
import one.superstack.controllable.response.PropertyExecutionResponse;
import one.superstack.controllable.service.PropertyValueService;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
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
        List<PropertyValue> propertyValues = fetchValues(request);

        // Construct the trees
        PropertyValueTrees trees = constructTrees(propertyValues);

        // Evaluate the trees
        return evaluateTrees(request, trees);
    }

    private List<PropertyValue> fetchValues(AugmentedBulkPropertyExecutionRequest request) {
        List<Criteria> criteriaList = new ArrayList<>(); // Or condition list for fetching multiple properties

        for (AugmentedPropertyExecutionRequest augmentedPropertyExecutionRequest : request.getRequests()) {
            Property property = augmentedPropertyExecutionRequest.getProperty().getProperty();
            Criteria criteria = Criteria
                    .where("propertyId").is(property.getId()); // Individual property id

            if (null != property.getSegmentTreeStructure() && null != property.getSegmentTreeStructure().getLevels() && !property.getSegmentTreeStructure().getLevels().isEmpty()) {
                // Segment query
                criteria = criteria.andOperator(constructSegmentFilter(property, augmentedPropertyExecutionRequest.getParams()));
            } else {
                criteria = criteria.and("segment").isNull();
            }

            criteriaList.add(criteria);
        }

        Query cumulativeQuery = Query.query(Criteria
                .where("environmentId").is(request.getEnvironment().getId()) // Common query criteria
                .orOperator(criteriaList));

        return propertyValueService.get(cumulativeQuery);
    }

    private List<Criteria> constructSegmentFilter(Property property, Map<String, Object> params) {
        List<Criteria> segmentFilter = new ArrayList<>();

        int currentSegmentTreeLevelIndex = 0;

        for (SegmentTreeLevel segmentTreeLevel : property.getSegmentTreeStructure().getLevels()) {
            String segmentTreeLevelName = segmentTreeLevel.getName();
            Object segmentPathComponentValue = params.getOrDefault(segmentTreeLevelName, null);

            String segmentPathValueField = String.format("segment.path.%d.value", currentSegmentTreeLevelIndex);
            String segmentPathNameField = String.format("segment.path.%d.name", currentSegmentTreeLevelIndex);

            Criteria segmentComponentCriteria = Criteria
                    .where(segmentPathNameField).is(segmentTreeLevelName)
                    .orOperator(
                            Criteria.where(segmentPathValueField).is(segmentPathComponentValue),
                            Criteria.where(segmentPathValueField).is(PropertyValueService.DEFAULT_SEGMENT)
                    );

            segmentFilter.add(segmentComponentCriteria);
            currentSegmentTreeLevelIndex++;
        }

        return segmentFilter;
    }

    private PropertyValueTrees constructTrees(List<PropertyValue> propertyValues) {
        // TODO
        return null;
    }

    private BulkPropertyExecutionResponse evaluateTrees(AugmentedBulkPropertyExecutionRequest request, PropertyValueTrees valueTrees) {
        // TODO
        return null;
    }

    private PropertyExecutionResponse evaluateTree(AugmentedPropertyExecutionRequest request, PropertyValueTreeNode root) {
        // TODO
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
