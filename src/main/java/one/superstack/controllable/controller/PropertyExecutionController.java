package one.superstack.controllable.controller;

import jakarta.validation.Valid;
import one.superstack.controllable.auth.app.AuthenticatedAppController;
import one.superstack.controllable.request.BulkPropertyExecutionRequest;
import one.superstack.controllable.response.BulkPropertyExecutionResponse;
import one.superstack.controllable.service.PropertyExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class PropertyExecutionController extends AuthenticatedAppController {

    private final PropertyExecutionService propertyExecutionService;

    @Autowired
    public PropertyExecutionController(PropertyExecutionService propertyExecutionService) {
        this.propertyExecutionService = propertyExecutionService;
    }

    @PostMapping(value = "/properties/execute")
    public BulkPropertyExecutionResponse execute(@Valid @RequestBody BulkPropertyExecutionRequest request) {
        return propertyExecutionService.execute(request, getApp());
    }
}
