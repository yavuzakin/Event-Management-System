package yte.intern.project.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.common.enums.MessageType;
import yte.intern.project.event.entity.Event;
import yte.intern.project.event.repository.EventRepository;
import yte.intern.project.security.service.CustomUserDetailsService;
import yte.intern.project.security.util.JwtUtil;
import yte.intern.project.users.controller.request.LoginRequest;
import yte.intern.project.users.entity.Users;
import yte.intern.project.users.controller.request.RegisterRequest;
import yte.intern.project.users.repository.UsersRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class UsersService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    private final UsersRepository usersRepository;
    private final EventRepository eventRepository;

    public UsersService(UsersRepository usersRepository, EventRepository eventRepository) {
        this.usersRepository = usersRepository;
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .filter(event -> {
                    return event.eventStartDate().isAfter(LocalDate.now());
                })
                .toList();
    }

    public MessageResponse register(RegisterRequest registerRequest) {
        Users userFromDB = usersRepository.findByUsername(registerRequest.username());
        if(userFromDB != null) {
            return new MessageResponse(MessageType.ERROR, "There is a user with %s username".formatted(registerRequest.username()));
        }
        usersRepository.save(Users.toUser(registerRequest));
        return new MessageResponse(MessageType.SUCCESS, "User %s has been registered successfully.".formatted(registerRequest.username()));
    }

    public String login(LoginRequest loginRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = customUserDetailsService
                .loadUserByUsername(loginRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails);

        return jwt;
    }
}
