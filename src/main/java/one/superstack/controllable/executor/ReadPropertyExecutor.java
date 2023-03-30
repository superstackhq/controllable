package one.superstack.controllable.executor;

import one.superstack.controllable.auth.app.AuthenticatedApp;
import one.superstack.controllable.embedded.Segment;
import one.superstack.controllable.embedded.SegmentPathComponent;
import one.superstack.controllable.embedded.SegmentTreeLevel;
import one.superstack.controllable.embedded.SegmentTreeStructure;
import one.superstack.controllable.model.Property;
import one.superstack.controllable.model.PropertyValue;
import one.superstack.controllable.pojo.*;
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
import java.util.HashMap;
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
        Map<String, PropertyValueTreeNode> roots = new HashMap<>();

        for (PropertyValue propertyValue : propertyValues) {
            String propertyId = propertyValue.getPropertyId();
            Segment segment = propertyValue.getSegment();

            // Fetch the root node for the property
            PropertyValueTreeNode rootNode = roots.getOrDefault(propertyId, null);

            if (rootNode == null) {
                // If root is not initialized for property, then initialize it
                rootNode = new PropertyValueTreeNode();
                roots.put(propertyId, rootNode);
            }

            // Traverse the segment tree from the root
            PropertyValueTreeNode currentNode = rootNode;

            if (null != segment && null != segment.getPath() && !segment.getPath().isEmpty()) {
                for (SegmentPathComponent segmentPathComponent : segment.getPath()) {
                    String segmentPathComponentValue = null;
                    if (null != segmentPathComponent.getValue()) {
                        segmentPathComponentValue = segmentPathComponent.getValue().toString();
                    }

                    // Fetch the child node based on the segment
                    PropertyValueTreeNode childNode = currentNode.getChildren().getOrDefault(segmentPathComponentValue, null);

                    if (null == childNode) {
                        // If child not is not initialized, initialize it
                        childNode = new PropertyValueTreeNode();
                        currentNode.getChildren().put(segmentPathComponentValue, childNode);
                    }

                    // Set child node as the current
                    currentNode = childNode;
                }
            }

            // Append the property value at the last traversed node
            currentNode.getValues().add(propertyValue);
        }

        return new PropertyValueTrees(roots);
    }

    private BulkPropertyExecutionResponse evaluateTrees(AugmentedBulkPropertyExecutionRequest request, PropertyValueTrees valueTrees) {
        List<PropertyExecutionResponse> propertyExecutionResponses = new ArrayList<>();

        for (AugmentedPropertyExecutionRequest augmentedPropertyExecutionRequest : request.getRequests()) {
            String propertyId = augmentedPropertyExecutionRequest.getProperty().getProperty().getId();

            PropertyValueTreeNode rootNode = valueTrees.getRoots().getOrDefault(propertyId, null);

            if (null == rootNode) {
                // If root node does not exist for the property, abort search
                propertyExecutionResponses.add(new PropertyExecutionResponse(true, "segment tree root not found", null));
            } else {
                // Evaluate the tree starting from the root node for the property
                propertyExecutionResponses.add(evaluateTree(augmentedPropertyExecutionRequest, rootNode));
            }
        }

        return new BulkPropertyExecutionResponse(propertyExecutionResponses);
    }

    private PropertyExecutionResponse evaluateTree(AugmentedPropertyExecutionRequest request, PropertyValueTreeNode root) {
        Map<String, Object> params = request.getParams();
        SegmentTreeStructure segmentTreeStructure = request.getProperty().getProperty().getSegmentTreeStructure();

        // Start with the root
        PropertyValueTreeNode currentNode = root;

        if (null != segmentTreeStructure && null != segmentTreeStructure.getLevels() && !segmentTreeStructure.getLevels().isEmpty()) {
            // If segment tree exists, traverse the segment tree
            for (SegmentTreeLevel segmentTreeLevel : segmentTreeStructure.getLevels()) {
                Map<String, PropertyValueTreeNode> children = currentNode.getChildren();

                // No children - abort search
                if (children.isEmpty()) {
                    return new PropertyExecutionResponse(true, "no children found for segment during segment tree traversal", null);
                }

                // Find the segment component in the children to determine next node
                Object segmentPathComponentValue = params.getOrDefault(segmentTreeLevel.getName(), null);
                String segmentPathComponentValueString = null;
                if (null != segmentPathComponentValue) {
                    segmentPathComponentValueString = segmentPathComponentValue.toString();
                }

                PropertyValueTreeNode nextNode = children.getOrDefault(segmentPathComponentValueString, null);

                if (null == nextNode) {
                    // If child node is not found, fallback to default segment

                    nextNode = children.getOrDefault(PropertyValueService.DEFAULT_SEGMENT, null);

                    // If default segment child does not exist as well - abort search
                    if (null == nextNode) {
                        return new PropertyExecutionResponse(true, "no property value found against segment and default segment", null);
                    }
                }

                // Set current node as next node
                currentNode = nextNode;
            }
        }

        // We reached the end of the segment tree path, let's look for eligible property values
        List<PropertyValue> eligiblePropertyValues = currentNode.getValues();

        if (null == eligiblePropertyValues || eligiblePropertyValues.isEmpty()) {
            // If eligible values list is empty - abort
            return new PropertyExecutionResponse(true, "no property values found before rule filtering", null);
        }

        try {
            // Apply the rules and filter the property values
            PropertyValue filteredValue = applyRules(eligiblePropertyValues, params);

            if (null == filteredValue) {
                // If no rules passed - abort
                return new PropertyExecutionResponse(true, "no rules passed", null);
            }

            // We have a hit!
            return new PropertyExecutionResponse(true, null, new PropertyExecutionValue(filteredValue));
        } catch (Throwable e) {
            // It was really a system failure
            return new PropertyExecutionResponse(false, e.getMessage(), null);
        }
    }

    private PropertyValue applyRules(List<PropertyValue> propertyValues, Map<String, Object> params) throws ScriptException {
        PropertyValue valueWithEmptyRule = null;
        PropertyValue valueWithPassingRule = null;

        for (PropertyValue propertyValue : propertyValues) {
            if (null == propertyValue.getRule() || null == propertyValue.getRule().getExpression() || propertyValue.getRule().getExpression().isBlank()) {
                // Rule-less property
                if (null == valueWithEmptyRule) {
                    valueWithEmptyRule = propertyValue;
                }
            } else {
                // Evaluate the rule
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

        // The one with the rule gets higher priority
        if (null != valueWithPassingRule) {
            return valueWithPassingRule;
        }

        return valueWithEmptyRule;
    }
}
