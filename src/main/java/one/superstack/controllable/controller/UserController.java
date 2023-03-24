package one.superstack.controllable.controller;

import jakarta.validation.Valid;
import one.superstack.controllable.auth.AuthenticatedUserController;
import one.superstack.controllable.model.User;
import one.superstack.controllable.request.AdminChangeRequest;
import one.superstack.controllable.request.PasswordChangeRequest;
import one.superstack.controllable.request.UserAdditionRequest;
import one.superstack.controllable.response.UserPasswordResponse;
import one.superstack.controllable.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class UserController extends AuthenticatedUserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users/me")
    public User get() throws Throwable {
        return userService.get(getUserId());
    }

    @PutMapping(value = "/users/me/password")
    public User changePassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest) throws Throwable {
        return userService.changePassword(getUserId(), passwordChangeRequest);
    }

    @PostMapping(value = "/users")
    public UserPasswordResponse add(@Valid @RequestBody UserAdditionRequest userAdditionRequest) {
        checkAdmin();
        return userService.add(userAdditionRequest, getUser());
    }

    @GetMapping(value = "/users/{userId}")
    public User get(@PathVariable String userId) throws Throwable {
        return userService.get(userId, getOrganizationId());
    }

    @PutMapping(value = "/users/{userId}/admin")
    public User changeAdmin(@PathVariable String userId, @Valid @RequestBody AdminChangeRequest adminChangeRequest) throws Throwable {
        checkAdmin();
        return userService.changeAdmin(userId, adminChangeRequest, getOrganizationId());
    }

    @PutMapping(value = "/users/{userId}/password")
    public UserPasswordResponse resetPassword(@PathVariable String userId) throws Throwable {
        checkAdmin();
        return userService.resetPassword(userId, getOrganizationId());
    }

    @DeleteMapping(value = "/users/{userId}")
    public User delete(@PathVariable String userId) throws Throwable {
        checkAdmin();
        return userService.delete(userId, getOrganizationId());
    }

    @GetMapping(value = "/users")
    public List<User> list(Pageable pageable) {
        return userService.list(getOrganizationId(), pageable);
    }
}
