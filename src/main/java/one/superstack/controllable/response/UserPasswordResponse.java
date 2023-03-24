package one.superstack.controllable.response;

import one.superstack.controllable.model.User;

import java.io.Serializable;

public class UserPasswordResponse implements Serializable {

    private String password;

    private User user;

    public UserPasswordResponse() {

    }

    public UserPasswordResponse(String password, User user) {
        this.password = password;
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
