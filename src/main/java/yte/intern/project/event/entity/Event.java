package yte.intern.project.event.entity;

import lombok.Getter;
import lombok.experimental.Accessors;
import yte.intern.project.common.entity.BaseEntity;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Getter
@Accessors(fluent = true)
public class Event extends BaseEntity {

    private String eventName;
    private LocalDate eventStartDate;
    private LocalDate eventFinishDate;

    protected Event() {
    }

    public Event(String eventName, LocalDate eventStartDate, LocalDate eventFinishDate) {
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
        this.eventFinishDate = eventFinishDate;
    }

    public void updateEvent(Event updatedEvent) {
        this.eventName = updatedEvent.eventName();
        this.eventStartDate = updatedEvent.eventStartDate();
        this.eventFinishDate = updatedEvent.eventFinishDate();
    }
}
