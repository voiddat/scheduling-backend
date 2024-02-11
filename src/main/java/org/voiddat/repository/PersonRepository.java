package org.voiddat.repository;

import org.voiddat.exception.NonExistentPersonException;
import org.voiddat.model.Person;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PersonRepository {
    // this field is like a database for sake of simplicity
    private static final Set<Person> PERSONS = new HashSet<>();

    // for simplicity all methods are static - usually repository class is a Spring bean with singleton scope
    public static Person addPerson(Person person) throws SQLIntegrityConstraintViolationException {
        checkEmailUniqueness(person.email());
        PERSONS.add(person);
        return person;
    }

    public static boolean isPersonAvailableAtGivenTimeslot(Person person, LocalDateTime timeslot) {
        return person.meetingIds()
                .stream()
                .map(MeetingRepository::getMeeting)
                .flatMap(Optional::stream)
                .noneMatch(meeting -> meeting.timeslot().equals(timeslot));
    }

    public static Set<Person> getPersons(Set<Long> personIds) {
        Set<Person> persons = PERSONS
                .stream()
                .filter(person -> personIds.contains(person.id()))
                .collect(Collectors.toSet());

        if (persons.size() != personIds.size()) {
            throw new NonExistentPersonException("Error: At least one person does not exist");
        }

        return persons;
    }

    public static Optional<Person> getPerson(Long personId) {
        return PERSONS
                .stream()
                .filter(person -> personId.equals(person.id())).findAny();
    }

    // should be provided by database constraint
    private static void checkEmailUniqueness(String email) throws SQLIntegrityConstraintViolationException {
        if (PERSONS.stream().anyMatch(person -> person.email().equals(email))) {
            throw new SQLIntegrityConstraintViolationException("Error: This email already exists in the database");
        }
    }
}
