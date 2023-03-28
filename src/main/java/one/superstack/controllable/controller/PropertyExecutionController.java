package one.superstack.controllable.controller;

import one.superstack.controllable.auth.app.AuthenticatedAppController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class PropertyExecutionController extends AuthenticatedAppController {

}
