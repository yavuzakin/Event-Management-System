package yte.intern.project.users.controller.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;


@Getter
public class LoginRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
