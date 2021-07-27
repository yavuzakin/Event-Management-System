package yte.intern.project.event.controller.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import yte.intern.project.event.entity.Event;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@RequiredArgsConstructor
@ToString
@Getter
public class AddEventRequest {

    @Size(max = 255, message = "Event name cannot exceed 255 characters")
    @NotEmpty(message = "Event name field cannot be empty")
    private final String eventName;

    @FutureOrPresent(message = "Must be a present or a future date")
    private final LocalDate eventStartDate;

    @FutureOrPresent(message = "Must be a present or a future date")
    private final LocalDate eventFinishDate;

    @Max(value = 5, message = "Event quota cannot be higher than 5")
    private final Integer eventQuota;

    public Event toEvent() {
        return new Event(eventName, eventStartDate, eventFinishDate, eventQuota, Set.of());
    }

}
