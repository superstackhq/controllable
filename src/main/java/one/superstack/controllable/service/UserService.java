package one.superstack.controllable.service;

import one.superstack.controllable.auth.AuthenticatedUser;
import one.superstack.controllable.auth.Jwt;
import one.superstack.controllable.exception.AuthenticationException;
import one.superstack.controllable.exception.ClientException;
import one.superstack.controllable.exception.NotFoundException;
import one.superstack.controllable.model.Organization;
import one.superstack.controllable.model.User;
import one.superstack.controllable.repository.UserRepository;
import one.superstack.controllable.request.*;
import one.superstack.controllable.response.AuthenticationResponse;
import one.superstack.controllable.response.UserPasswordResponse;
import one.superstack.controllable.util.Password;
import one.superstack.controllable.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final OrganizationService organizationService;

    private final Jwt jwt;

    @Autowired
    public UserService(UserRepository userRepository, OrganizationService organizationService, Jwt jwt) {
        this.userRepository = userRepository;
        this.organizationService = organizationService;
        this.jwt = jwt;
    }

    public User signUp(SignUpRequest signUpRequest) {
        if (organizationService.nameExists(signUpRequest.getOrganizationName())) {
            throw new ClientException("Organization " + signUpRequest.getOrganizationName() + " already exists");
        }

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getPassword(),
                true, null, null);
        user = userRepository.save(user);

        Organization organization = organizationService.create(signUpRequest.getOrganizationName(), user.getId());
        user.setOrganizationId(organization.getId());

        return userRepository.save(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws Throwable {
        Organization organization = organizationService.getByName(authenticationRequest.getOrganizationName());

        User user = userRepository.findByUsernameAndOrganizationId(authenticationRequest.getUsername(), organization.getId())
                .orElseThrow((Supplier<Throwable>) AuthenticationException::new);

        if (!Password.verify(authenticationRequest.getPassword(), user.getPassword())) {
            throw new AuthenticationException();
        }

        return new AuthenticationResponse(jwt.getUserToken(new AuthenticatedUser(user.getId(), user.getOrganizationId(), user.getAdmin())));
    }

    public User get(String userId) throws Throwable {
        return userRepository.findById(userId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("User not found"));
    }

    public User changePassword(String userId, PasswordChangeRequest passwordChangeRequest) throws Throwable {
        User user = get(userId);

        user.setPassword(user.getPassword());
        user.setModifiedOn(new Date());

        return userRepository.save(user);
    }

    public UserPasswordResponse add(UserAdditionRequest userAdditionRequest, AuthenticatedUser creator) {
        if (usernameExists(userAdditionRequest.getUsername(), creator.getOrganizationId())) {
            throw new ClientException("Username " + userAdditionRequest.getUsername() + " is already taken");
        }

        String password = Random.generateRandomString(16);

        User user = new User(userAdditionRequest.getUsername(), password, userAdditionRequest.getAdmin(), creator.getOrganizationId(), creator.getId());
        user = userRepository.save(user);

        return new UserPasswordResponse(password, user);
    }

    public User get(String userId, String organizationId) throws Throwable {
        return userRepository.findByIdAndOrganizationId(userId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("User not found"));
    }

    public User changeAdmin(String userId, AdminChangeRequest adminChangeRequest, String organizationId) throws Throwable {
        User user = get(userId, organizationId);
        user.setAdmin(adminChangeRequest.getAdmin());
        user.setModifiedOn(new Date());
        return userRepository.save(user);
    }

    public UserPasswordResponse resetPassword(String userId, String organizationId) throws Throwable {
        User user = get(userId, organizationId);

        String password = Random.generateRandomString(16);

        user.setPassword(password);
        user.setModifiedOn(new Date());
        user = userRepository.save(user);

        return new UserPasswordResponse(password, user);
    }

    public User delete(String userId, String organizationId) throws Throwable {
        User user = get(userId, organizationId);
        userRepository.delete(user);
        return user;
    }

    public List<User> list(String organizationId, Pageable pageable) {
        return userRepository.findByOrganizationId(organizationId, pageable);
    }

    private Boolean usernameExists(String username, String organizationId) {
        return userRepository.existsByUsernameAndOrganizationId(username, organizationId);
    }
}
