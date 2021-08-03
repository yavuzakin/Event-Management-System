package yte.intern.project.users.controller.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import yte.intern.project.users.enums.UserType;

import javax.validation.constraints.*;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class RegisterRequest {

    @NotNull
    @NotEmpty
    @Size(min = 3, max = 9, message = "First name must be between 3 and 9 characters")
    private final String firstName;
    @Size(min = 3, max = 9, message = "Last name must be between 3 and 9 characters")
    private final String lastName;
    @Size(min = 11, max = 11, message = "Social security number must be 11 characters long")
    private final String ssnNo;
    @Email
    private final String email;
    @Size(min= 3, max= 10, message = "Username must be between 3 and 10 characters")
    private final String username;
    @Size(min= 3, max= 10, message = "Password must be between 3 and 10 characters")
    private final String password;
    @Min(value= 14, message = "Minimum value is 14")
    @Max(value= 68, message = "Maximum value is 68")
    private final Integer age;
    private final UserType userType;
}
