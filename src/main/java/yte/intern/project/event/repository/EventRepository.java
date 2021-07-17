package yte.intern.project.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.event.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

}
