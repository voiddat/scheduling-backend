package org.voiddat.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public record Meeting(Long id, Set<Person> persons, LocalDateTime timeslot) {
    private static final AtomicLong autoIncrementId = new AtomicLong(0);

    public Meeting(Set<Person> persons, LocalDateTime timeslot) {
        this(autoIncrementId.incrementAndGet(), persons, timeslot);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return Objects.equals(id, meeting.id) && Objects.equals(persons, meeting.persons) && Objects.equals(timeslot, meeting.timeslot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, persons, timeslot);
    }
}
