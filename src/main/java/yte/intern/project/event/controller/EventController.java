package yte.intern.project.event.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.event.controller.request.AddEventRequest;
import yte.intern.project.event.controller.request.UpdateEventRequest;
import yte.intern.project.event.controller.response.EventQueryResponse;
import yte.intern.project.event.service.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(final EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventQueryResponse> getAllEvents() {
        return eventService.getAllEvents()
                .stream()
                .map(EventQueryResponse::new)
                .toList();
    }

    @PostMapping
    public MessageResponse addEvent(@Valid @RequestBody final AddEventRequest request) {
        return eventService.addEvent(request.toEvent());
    }

    @PutMapping("/{id}")
    public MessageResponse updateEvent(@PathVariable Long id, @RequestBody @Valid final UpdateEventRequest request) {
        return eventService.updateEvent(id, request.toEvent());
    }

    @DeleteMapping("/{id}")
    public MessageResponse deleteEvent(@PathVariable Long id) {
        return eventService.deleteEvent(id);
    }
}
