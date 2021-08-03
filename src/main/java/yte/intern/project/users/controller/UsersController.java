package yte.intern.project.users.controller;

import com.google.zxing.WriterException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.event.controller.request.AddEventRequest;
import yte.intern.project.event.controller.response.EventQueryResponse;
import yte.intern.project.users.controller.request.LoginRequest;
import yte.intern.project.users.controller.request.RegisterRequest;
import yte.intern.project.users.controller.response.UserQueryResponse;
import yte.intern.project.users.entity.Users;
import yte.intern.project.users.service.UsersService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
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
    public MessageResponse register(@RequestBody @Valid RegisterRequest registerRequest) {
        return usersService.register(registerRequest);
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest loginRequest) throws Exception {
        return usersService.login(loginRequest);
    }

    @PostMapping("/registerToEvent/{username}/{eventId}")
    public MessageResponse registerToEvent(@PathVariable("username") String username, @PathVariable("eventId") Long eventId) throws Exception {
        return usersService.registerToEvent(username, eventId);
    }

    @PostMapping("/unregisterFromEvent/{username}/{eventId}")
    public MessageResponse unregisterFromEvent(@PathVariable("username") String username, @PathVariable("eventId") Long eventId) {
        return usersService.unregisterFromEvent(username, eventId);
    }

    @GetMapping("/getRegisteredEvents/{username}")
    public List<EventQueryResponse> getRegisteredEvents(@PathVariable String username) {
        return usersService.getRegisteredEvents(username);
    }

    @GetMapping("getUnregisteredEvents/{username}")
    public List<EventQueryResponse> getUnregisteredEvents(@PathVariable String username) {
        return usersService.getUnregisteredEvents(username);
    }

    @GetMapping("/getUserByUsername/{username}")
    public UserQueryResponse getUserByUsername(@PathVariable String username) {
        return usersService.getUserByUsername(username);
    }

    @GetMapping("/userCount")
    public Integer getUserCount() {
        return usersService.getUserCount();
    }
}
