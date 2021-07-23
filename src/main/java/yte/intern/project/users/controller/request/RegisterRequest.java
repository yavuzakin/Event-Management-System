package yte.intern.project.users.controller.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import yte.intern.project.users.enums.UserType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class RegisterRequest {

    @NotNull
    @NotEmpty
    @Size(min = 3, max = 9, message = "")
    private final String firstName;
    @Size(min = 3, max = 9, message = "")
    private final String lastName;
    private final String ssnNo;
    @Email
    private final String email;
    private final String username;
    private final String password;
    private final Integer age;
    private final UserType userType;
}
