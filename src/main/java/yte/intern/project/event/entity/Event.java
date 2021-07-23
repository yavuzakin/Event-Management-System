package yte.intern.project.event.entity;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import yte.intern.project.common.entity.BaseEntity;
import yte.intern.project.users.entity.Users;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Accessors(fluent = true)
@ToString
public class Event extends BaseEntity {

    private String eventName;
    private LocalDate eventStartDate;
    private LocalDate eventFinishDate;
    private Integer eventQuota;

    /*@ManyToMany(mappedBy = "events")
    private Set<Users> users = new HashSet<>();*/

    protected Event() {
    }

    public Event(String eventName, LocalDate eventStartDate, LocalDate eventFinishDate, Integer eventQuota) {
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
        this.eventFinishDate = eventFinishDate;
        this.eventQuota = eventQuota;
    }

    public void updateEvent(Event updatedEvent) {
        this.eventName = updatedEvent.eventName();
        this.eventStartDate = updatedEvent.eventStartDate();
        this.eventFinishDate = updatedEvent.eventFinishDate();
        this.eventQuota = updatedEvent.eventQuota();
    }
}
