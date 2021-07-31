package yte.intern.project.users.controller.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import yte.intern.project.users.entity.Users;

@RequiredArgsConstructor
@Getter
public class UserQueryResponse {
    private final String firstName;
    private final String lastName;
    private final String ssnNo;
    private final String email;
    private final String username;
    private final String password;
    private final Integer age;

    public UserQueryResponse(Users user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.ssnNo = user.getSsnNo();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.age = user.getAge();
    }
}
