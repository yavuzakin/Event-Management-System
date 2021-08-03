package yte.intern.project.users.controller.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
public class LoginRequest {
    @NotNull
    @Size(min= 3, max= 10, message = "Username must be between 3 and 10 characters")
    private String username;

    @NotNull
    @Size(min= 3, max= 10, message = "Password must be between 3 and 10 characters")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
