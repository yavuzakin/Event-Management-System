package yte.intern.project.users.entity;

import lombok.Getter;
import yte.intern.project.common.entity.BaseEntity;
import yte.intern.project.users.enums.UserType;
import yte.intern.project.users.controller.request.RegisterRequest;

import javax.persistence.Entity;

@Entity
@Getter
public class Users extends BaseEntity {
    private String firstName;
    private String lastName;
    private String ssnNo;
    private String email;
    private String username;
    private String password;
    private Integer age;
    private UserType userType;

    /*@ManyToMany
    @JoinTable(name = "registered_events",
            joinColumns = {@JoinColumn(name = "participant_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    private Set<Event> events = new HashSet<>();*/

    protected Users() {
    }

    public Users(String firstName,
                 String lastName,
                 String ssnNo,
                 String email,
                 String username,
                 String password,
                 Integer age,
                 UserType userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssnNo = ssnNo;
        this.email = email;
        this.username = username;
        this.password = password;
        this.age = age;
        this.userType = userType;
    }

    public static Users toUser(RegisterRequest registerRequest) {
        return new Users(registerRequest.firstName(),
                registerRequest.lastName(),
                registerRequest.ssnNo(),
                registerRequest.email(),
                registerRequest.username(),
                registerRequest.password(),
                registerRequest.age(),
                registerRequest.userType());
    }
}
