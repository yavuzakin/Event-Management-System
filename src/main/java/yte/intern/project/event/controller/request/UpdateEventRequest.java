package yte.intern.project.event.controller.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import yte.intern.project.event.entity.Event;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class UpdateEventRequest {

    @Size(max = 255, message = "Event name cannot exceed 255 characters")
    @NotEmpty(message = "Event name field cannot be empty")
    private final String eventName;

    @FutureOrPresent(message = "Must be a present or a future date")
    private final LocalDate eventStartDate;

    @FutureOrPresent
    private final LocalDate eventFinishDate;


    public Event toEvent() {
        return new Event(eventName, eventStartDate, eventFinishDate);
    }
}
