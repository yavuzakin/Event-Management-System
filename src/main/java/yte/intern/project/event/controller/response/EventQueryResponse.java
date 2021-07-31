package yte.intern.project.event.controller.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import yte.intern.project.event.entity.Event;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
@ToString
public class EventQueryResponse {
    private final Long id;
    private final String eventName;
    private final LocalDate eventStartDate;
    private final LocalDate eventFinishDate;
    private final Integer eventQuota;

    public EventQueryResponse(Event event) {
        this.id = event.id();
        this.eventName = event.eventName();
        this.eventStartDate = event.eventStartDate();
        this.eventFinishDate = event.eventFinishDate();
        this.eventQuota = event.eventQuota();
    }
}
