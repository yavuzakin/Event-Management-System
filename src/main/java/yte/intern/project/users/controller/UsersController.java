package yte.intern.project.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.event.controller.response.EventQueryResponse;
import yte.intern.project.security.service.CustomUserDetailsService;
import yte.intern.project.security.util.JwtUtil;
import yte.intern.project.users.controller.request.LoginRequest;
import yte.intern.project.users.controller.request.RegisterRequest;
import yte.intern.project.users.service.UsersService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/getAllEvents")
    public List<EventQueryResponse> getAllEvents() {
        return usersService.getAllEvents()
                .stream()
                .map(EventQueryResponse::new)
                .toList();
    }

    @PostMapping("/register")
    public MessageResponse register(@RequestBody RegisterRequest registerRequest) {
        return usersService.register(registerRequest);
    }

    @PostMapping("/authenticate")
    public String login(@RequestBody LoginRequest loginRequest) throws Exception {
        return usersService.login(loginRequest);
    }
}
