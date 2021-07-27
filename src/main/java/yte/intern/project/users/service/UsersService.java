package yte.intern.project.users.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.common.enums.MessageType;
import yte.intern.project.event.controller.request.AddEventRequest;
import yte.intern.project.event.controller.response.EventQueryResponse;
import yte.intern.project.event.entity.Event;
import yte.intern.project.event.repository.EventRepository;
import yte.intern.project.security.domain.Authority;
import yte.intern.project.security.repository.AuthorityRepository;
import yte.intern.project.security.util.JwtUtil;
import yte.intern.project.users.controller.request.LoginRequest;
import yte.intern.project.users.entity.Users;
import yte.intern.project.users.controller.request.RegisterRequest;
import yte.intern.project.users.enums.UserType;
import yte.intern.project.users.repository.UsersRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class UsersService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    private final AuthenticationManager authenticationManager;
    private final UsersRepository usersRepository;
    private final EventRepository eventRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(final AuthenticationManager authenticationManager,
                        final UsersRepository usersRepository,
                        final EventRepository eventRepository,
                        final AuthorityRepository authorityRepository,
                        final PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
        this.eventRepository = eventRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<EventQueryResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .filter(event -> {
                    return event.eventStartDate().isAfter(LocalDate.now());
                })
                .map(EventQueryResponse::new)
                .toList();
    }

    public MessageResponse register(RegisterRequest registerRequest) {

        Users userFromDB = usersRepository.findByUsername(registerRequest.username())
                .orElse(null);

        if(userFromDB != null) {
            return new MessageResponse(MessageType.ERROR, "There is a user with %s username".formatted(registerRequest.username()));
        }

        Authority adminAuthority = authorityRepository.findByAuthority("ADMIN");
        Authority userAuthority = authorityRepository.findByAuthority("USER");

        if(registerRequest.userType() == UserType.USER) {
            usersRepository.save(Users.toUser(registerRequest, Set.of(userAuthority), Set.of()));
        } else if(registerRequest.userType() == UserType.ADMIN) {
            usersRepository.save(Users.toUser(registerRequest, Set.of(adminAuthority, userAuthority), Set.of()));
        }

        return new MessageResponse(MessageType.SUCCESS, "User %s has been registered successfully.".formatted(registerRequest.username()));
    }

    public String login(LoginRequest loginRequest) throws Exception {
        var token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        try {
            Authentication authenticatedToken = authenticationManager.authenticate(token);
            String jwt = JwtUtil.generateToken(authenticatedToken, secretKey);
            return jwt;
        } catch (AuthenticationException ex) {

        }
        return null;
    }

    public MessageResponse addEventToUser(String username, Long eventId) {
        Users userFromDB = usersRepository.findByUsername(username)
                .orElse(null);

        Event eventFromDB = eventRepository.findById(eventId)
                .orElse(null);

        if(userFromDB != null && eventFromDB != null) {
            ///userFromDB.addEvent(eventFromDB);
            userFromDB.getEvents().add(eventFromDB);
            userFromDB.updateUser(userFromDB);
            usersRepository.save(userFromDB);
            System.out.println(userFromDB.toString());
            return new MessageResponse(MessageType.SUCCESS, "User %s has been registered to event %s successfully".formatted(username, eventFromDB.eventName()));
        }

        return new MessageResponse(MessageType.ERROR, "User %s cannot be found".formatted(username));
    }

    public List<EventQueryResponse> getRegisteredEvents(String username) {
        Users userFromDB = usersRepository.findByUsername(username)
                .orElse(null);

        if(userFromDB != null) {
            return userFromDB.getEvents()
                    .stream()
                    .map(EventQueryResponse::new)
                    .toList();
        }

        return null;
    }
}
