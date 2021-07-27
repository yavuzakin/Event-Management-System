package yte.intern.project.users.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.event.controller.request.AddEventRequest;
import yte.intern.project.event.controller.response.EventQueryResponse;
import yte.intern.project.users.controller.request.LoginRequest;
import yte.intern.project.users.controller.request.RegisterRequest;
import yte.intern.project.users.service.UsersService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/getAllEvents")
    public List<EventQueryResponse> getAllEvents() {
        return usersService.getAllEvents();
    }

    @PostMapping("/register")
    public MessageResponse register(@RequestBody RegisterRequest registerRequest) {
        return usersService.register(registerRequest);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) throws Exception {
        return usersService.login(loginRequest);
    }

    @PostMapping("/addEventToUser/{username}/{eventId}")
    public MessageResponse addEventToUser(@PathVariable("username") String username, @PathVariable("eventId") Long eventId) {
        return usersService.addEventToUser(username, eventId);
    }

    @GetMapping("/getRegisteredEvents/{username}")
    public List<EventQueryResponse> getRegisteredEvents(@PathVariable String username) {
        return usersService.getRegisteredEvents(username);
    }
}
