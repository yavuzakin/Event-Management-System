package yte.intern.project.event.controller.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import yte.intern.project.event.entity.Event;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class EventQueryResponse {
    private final Long id;
    private final String eventName;
    private final LocalDate eventStartDate;
    private final LocalDate eventFinishDate;

    public EventQueryResponse(Event event) {
        this.id = event.id();
        this.eventName = event.eventName();
        this.eventStartDate = event.eventStartDate();
        this.eventFinishDate = event.eventFinishDate();
    }
}
