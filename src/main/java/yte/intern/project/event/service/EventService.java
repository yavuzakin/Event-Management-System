package yte.intern.project.event.service;

import org.springframework.stereotype.Service;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.common.enums.MessageType;
import yte.intern.project.event.entity.Event;
import yte.intern.project.event.repository.EventRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private static final String EVENT_ADDED_MESSAGE = "%s event has been added successfully";
    private static final String DATE_ERROR_MESSAGE = "The finish date %s cannot be earlier than the start date %s";
    private static final String EVENT_DOES_NOT_EXIST_MESSAGE = "Event with id %s does not exist";
    private static final String EVENT_UPDATED_MESSAGE = "Event with id %s has been updated successfully";
    private static final String EVENT_DELETED_MESSAGE = "Event with id %s has been deleted successfully";

    public EventService(final EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public MessageResponse addEvent(Event newEvent) {
        if(!isStartDateEarlierThanFinishDate(newEvent.eventStartDate(), newEvent.eventFinishDate()))
            return new MessageResponse(MessageType.ERROR, dateErrorMessage(newEvent.eventStartDate(), newEvent.eventFinishDate()));
        eventRepository.save(newEvent);
        return new MessageResponse(MessageType.SUCCESS, eventAddedMessage(newEvent.eventName()));
    }

    public boolean isStartDateEarlierThanFinishDate(LocalDate startDate, LocalDate finishDate) {
        return startDate.isBefore(finishDate);
    }

    public String dateErrorMessage(LocalDate startDate, LocalDate finishDate) {
        return DATE_ERROR_MESSAGE.formatted(finishDate.toString(), startDate.toString());
    }

    public String eventAddedMessage(String eventName) {
        return EVENT_ADDED_MESSAGE.formatted(eventName);
    }

    public MessageResponse updateEvent(Long id, Event updatedEvent) {
        Event eventFromDB = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EVENT_DOES_NOT_EXIST_MESSAGE.formatted(id)));
        if(!isStartDateEarlierThanFinishDate(updatedEvent.eventStartDate(), updatedEvent.eventFinishDate()))
            return new MessageResponse(MessageType.ERROR, dateErrorMessage(updatedEvent.eventStartDate(), updatedEvent.eventFinishDate()));
        /*if(eventFromDB.eventStartDate().isBefore(LocalDate.now()))
            return new MessageResponse(MessageType.ERROR, "Start date cannot be earlier than today");*/
        eventFromDB.updateEvent(updatedEvent);
        eventRepository.save(eventFromDB);
        return new MessageResponse(MessageType.SUCCESS, eventUpdatedMessage(id));
    }

    public String eventUpdatedMessage(Long id) {
        return EVENT_UPDATED_MESSAGE.formatted(id);
    }


    public MessageResponse deleteEvent(Long id) {
        if(!eventRepository.existsById(id))
            return new MessageResponse(MessageType.ERROR, EVENT_DOES_NOT_EXIST_MESSAGE.formatted(id));
        Event eventFromDB = eventRepository.findById(id).get();
        if(eventFromDB.eventStartDate().isBefore(LocalDate.now()))
            return new MessageResponse(MessageType.ERROR, "Start date cannot be earlier than today");
        eventRepository.deleteById(id);
        return new MessageResponse(MessageType.SUCCESS, EVENT_DELETED_MESSAGE.formatted(id));
    }
}
