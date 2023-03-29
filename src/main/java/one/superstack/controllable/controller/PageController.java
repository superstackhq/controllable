package one.superstack.controllable.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }

    @GetMapping(value = "/join")
    public String join() {
       return "join";
    }

    @GetMapping(value = "/logout")
    public String logout() {
        return "logout";
    }

    @GetMapping(value = "/home")
    public String home() {
        return "home";
    }

    @GetMapping(value = "/organization")
    public String organization() {
        return "organization";
    }

    @GetMapping(value = "/account")
    public String account() {
        return "account";
    }

    @GetMapping(value = "/users")
    public String users() {
        return "users";
    }

    @GetMapping(value = "/groups")
    public String groups() {
        return "groups";
    }

    @GetMapping(value = "/group-members")
    public String groupMembers() {
        return "group-members";
    }

    @GetMapping(value = "/api-access")
    public String apiAccess() {
        return "api-access";
    }

    @GetMapping(value = "/access")
    public String access() {
        return "access";
    }

    @GetMapping(value = "/actor-access")
    public String actorAccess() {
        return "actor-access";
    }

    @GetMapping(value = "/environment-access")
    public String environmentAccess() {
        return "environment-access";
    }

    @GetMapping(value = "/environments")
    public String environments() {
        return "environments";
    }

    @GetMapping(value = "/apps")
    public String apps() {
        return "apps";
    }

    @GetMapping(value = "/collection")
    public String collection() {
        return "collection";
    }

    @GetMapping(value = "/manage-collection")
    public String manageCollection() {
        return "manage-collection";
    }

    @GetMapping(value = "/collection-apps")
    public String collectionApps() {
        return "collection-apps";
    }

    @GetMapping(value = "/properties")
    public String properties() {
        return "properties";
    }

    @GetMapping(value = "/property-collections")
    public String propertyCollections() {
        return "property-collections";
    }

    @GetMapping(value = "/property-apps")
    public String propertyApps() {
        return "property-apps";
    }

    @GetMapping(value = "/property-values")
    public String propertyValues() {
        return "property-values";
    }

    @GetMapping(value = "/property-value-logs")
    public String propertyValueLogs() {
        return "property-value-logs";
    }
}
