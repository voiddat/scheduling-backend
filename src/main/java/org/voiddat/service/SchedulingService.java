package org.voiddat.service;

import org.voiddat.exception.NonExistentPersonException;
import org.voiddat.exception.UnavailableTimeslotException;
import org.voiddat.model.Meeting;
import org.voiddat.model.Person;
import org.voiddat.model.PersonsTimeslots;
import org.voiddat.model.Schedule;
import org.voiddat.repository.MeetingRepository;
import org.voiddat.repository.PersonRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SchedulingService {
    public Meeting createMeeting(Set<Long> personIds, LocalDateTime timeslot) {
        if (this.isNotWholeHour(timeslot)) {
            throw new IllegalArgumentException("Error: Please provide only full hours");
        }

        Set<Person> persons = PersonRepository.getPersons(personIds);

        if (persons
                .stream()
                .anyMatch(person -> !PersonRepository.isPersonAvailableAtGivenTimeslot(person, timeslot))) {
            throw new UnavailableTimeslotException("Error: At least one person is unavailable at given time");
        }

        Meeting meeting = new Meeting(persons, timeslot);
        persons.forEach(person -> person.meetingIds().add(meeting.id()));

        return MeetingRepository.saveMeeting(meeting);
    }

    public Schedule getPersonSchedule(Long personId) {
        Person person = PersonRepository.getPerson(personId)
                .orElseThrow(() -> new NonExistentPersonException(String.format("Error: Person id: %s does not exist", personId)));

        Comparator<Meeting> sortMeetingsByStartDateAscending = Comparator.comparing(Meeting::timeslot);

        List<Meeting> meetings = person.meetingIds()
                .stream()
                .map(MeetingRepository::getMeeting)
                .flatMap(Optional::stream)
                .filter(meeting -> meeting.timeslot().isAfter(LocalDateTime.now()))
                // sorting should be done on db query level
                .sorted(sortMeetingsByStartDateAscending)
                .toList();

        return new Schedule(person, meetings);
    }

    public PersonsTimeslots suggestAvailableTimeslots(Set<Long> personIds, int availableTimeslotsCount) {
        Set<Person> persons = personIds.stream()
                .map(PersonRepository::getPerson)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());

        Set<LocalDateTime> busyTimeslots = persons.stream()
                .flatMap(person -> person.meetingIds().stream())
                .map(MeetingRepository::getMeeting)
                .flatMap(Optional::stream)
                .map(Meeting::timeslot)
                .collect(Collectors.toSet());

        LocalDateTime nextFullHour = LocalDateTime.now()
                .plusHours(1)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        PersonsTimeslots personsTimeslots = new PersonsTimeslots(persons, new ArrayList<>());

        for (int i = 0; i < availableTimeslotsCount; i++) {
            while (busyTimeslots.contains(nextFullHour)) {
                nextFullHour = nextFullHour.plusHours(1);
            }
            personsTimeslots.timeslots().add(nextFullHour);
            nextFullHour = nextFullHour.plusHours(1);
        }

        return personsTimeslots;
    }

    private boolean isNotWholeHour(LocalDateTime timeslot) {
        return timeslot.getMinute() != 0 && timeslot.getSecond() != 0 && timeslot.getNano() != 0;
    }
}
