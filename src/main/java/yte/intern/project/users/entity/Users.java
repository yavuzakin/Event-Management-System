package yte.intern.project.users.entity;

import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;
import yte.intern.project.common.entity.BaseEntity;
import yte.intern.project.event.entity.Event;
import yte.intern.project.security.domain.Authority;
import yte.intern.project.users.enums.UserType;
import yte.intern.project.users.controller.request.RegisterRequest;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
public class Users extends BaseEntity implements UserDetails {
    private String firstName;
    private String lastName;
    private String ssnNo;
    private String email;
    private String username;
    private String password;
    private Integer age;
    private UserType userType;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "registered_events",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    private Set<Event> events;

    protected Users() {
    }

    public Users(String firstName,
                 String lastName,
                 String ssnNo,
                 String email,
                 String username,
                 String password,
                 Integer age,
                 UserType userType,
                 Set<Authority> authorities,
                 Set<Event> events) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssnNo = ssnNo;
        this.email = email;
        this.username = username;
        this.password = password;
        this.age = age;
        this.userType = userType;
        this.authorities = authorities;
        this.events = events;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static Users toUser(RegisterRequest registerRequest, Set<Authority> authorities, Set<Event> events) {
        return new Users(registerRequest.firstName(),
                registerRequest.lastName(),
                registerRequest.ssnNo(),
                registerRequest.email(),
                registerRequest.username(),
                registerRequest.password(),
                registerRequest.age(),
                registerRequest.userType(),
                authorities,
                events);
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public void updateUser(Users updatedUser) {
        this.firstName = updatedUser.getFirstName();
        this.lastName = updatedUser.getLastName();
        this.ssnNo = updatedUser.getSsnNo();
        this.email = updatedUser.getEmail();
        this.username = updatedUser.getUsername();
        this.password = updatedUser.getPassword();
        this.age = updatedUser.getAge();
        this.userType = updatedUser.getUserType();
        this.authorities = updatedUser.getAuthorities();
        this.events = updatedUser.getEvents();
    }

}
