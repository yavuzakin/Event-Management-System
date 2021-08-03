package yte.intern.project.users.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.common.email.SendEmailService;
import yte.intern.project.common.enums.MessageType;
import yte.intern.project.common.qrcode.QrcodeGenerator;
import yte.intern.project.event.controller.response.EventQueryResponse;
import yte.intern.project.event.entity.Event;
import yte.intern.project.event.repository.EventRepository;
import yte.intern.project.security.domain.Authority;
import yte.intern.project.security.repository.AuthorityRepository;
import yte.intern.project.security.util.JwtUtil;
import yte.intern.project.users.controller.request.LoginRequest;
import yte.intern.project.users.controller.response.UserQueryResponse;
import yte.intern.project.users.entity.Users;
import yte.intern.project.users.controller.request.RegisterRequest;
import yte.intern.project.users.enums.UserType;
import yte.intern.project.users.repository.UsersRepository;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private final SendEmailService sendEmailService;

    public UsersService(final AuthenticationManager authenticationManager,
                        final UsersRepository usersRepository,
                        final EventRepository eventRepository,
                        final AuthorityRepository authorityRepository,
                        final PasswordEncoder passwordEncoder,
                        final SendEmailService sendEmailService) {
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
        this.eventRepository = eventRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.sendEmailService = sendEmailService;
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
            return "error";
        }
    }

    public MessageResponse registerToEvent(String username, Long eventId) throws Exception {
        Users userFromDB = usersRepository.findByUsername(username)
                .orElse(null);

        Event eventFromDB = eventRepository.findById(eventId)
                .orElse(null);

        if(userFromDB != null && eventFromDB != null) {

            if(eventFromDB.users().size() >= eventFromDB.eventQuota())
                return new MessageResponse(MessageType.ERROR, "Quota is full!");

            userFromDB.getEvents().add(eventFromDB);
            userFromDB.updateUser(userFromDB);
            usersRepository.save(userFromDB);

            QrcodeGenerator qrcodeGenerator = new QrcodeGenerator(eventFromDB.eventName(), userFromDB.getFirstName(), userFromDB.getLastName());
            qrcodeGenerator.generateQrcode();

            sendEmail(userFromDB, eventFromDB);

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

    public List<EventQueryResponse> getUnregisteredEvents(String username) {
        List<EventQueryResponse> registeredEvents = getRegisteredEvents(username);
        List<EventQueryResponse> allEvents = getAllEvents();

        List<EventQueryResponse> unregisteredEvents = new ArrayList<>();
        Boolean isPresent = false;

        for(int i = 0 ; i < allEvents.size() ; i++) {
            for(int j = 0 ; j < registeredEvents.size() ; j++) {
                EventQueryResponse allEvent = allEvents.get(i);
                EventQueryResponse registeredEvent = registeredEvents.get(j);
                if(allEvents.get(i).getId() == registeredEvents.get(j).getId()) {
                    isPresent = true;
                    break;
                }
            }
            if(!isPresent) {
                EventQueryResponse itemToAdd = allEvents.get(i);
                unregisteredEvents.add(itemToAdd);
            }
            isPresent = false;
        }

        return unregisteredEvents;
    }

    public UserQueryResponse getUserByUsername(String username) {
        Users userFromDB = usersRepository.findByUsername(username)
                .orElse(null);

        if(userFromDB != null) {
            return new UserQueryResponse(userFromDB);
        }

        return null;
    }

    public MessageResponse sendEmail(Users userFromDB, Event eventFromDB) throws Exception {

        String body = "Dear " + userFromDB.getFirstName() + " " +userFromDB.getLastName() + ", thanks for joining us in the event of " + eventFromDB.eventName() +"!";
        String imageName = userFromDB.getFirstName() + userFromDB.getLastName() + eventFromDB.eventName();
        String imagePath = "C:\\Users\\yavuz\\Desktop\\TUBITAK\\Project\\project\\front-end\\public\\%s.jpg".formatted(imageName);

        try {
            sendEmailService.sendEmailWithImage(userFromDB.getEmail(), body, eventFromDB.eventName(), imagePath);
            return new MessageResponse(MessageType.SUCCESS, "Mail sent...");
        } catch (MessagingException err) {
            System.out.println(err);
        }

        return new MessageResponse(MessageType.ERROR, "Mail cannot be sent...");
    }

    public MessageResponse unregisterFromEvent(String username, Long eventId) {
        Users userFromDB = usersRepository.findByUsername(username)
                .orElse(null);

        Event eventFromDB = eventRepository.findById(eventId)
                .orElse(null);

        if(userFromDB != null && eventFromDB != null) {
            ///userFromDB.addEvent(eventFromDB);
            userFromDB.getEvents().remove(eventFromDB);
            userFromDB.updateUser(userFromDB);
            usersRepository.save(userFromDB);

            return new MessageResponse(MessageType.SUCCESS, "User %s has been unregistered from event %s successfully".formatted(username, eventFromDB.eventName()));
        }

        return new MessageResponse(MessageType.ERROR, "User %s cannot be found".formatted(username));
    }

    public Integer getUserCount() {
        List<Users> users = usersRepository.findAll();
        return users.size();
    }
}
