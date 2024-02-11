package org.voiddat.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record PersonsTimeslots(Set<Person> persons, List<LocalDateTime> timeslots) {
}
