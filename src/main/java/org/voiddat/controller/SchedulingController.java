package org.voiddat.controller;

import org.voiddat.model.Meeting;
import org.voiddat.model.PersonsTimeslots;
import org.voiddat.model.Schedule;
import org.voiddat.service.SchedulingService;

import java.time.LocalDateTime;
import java.util.Set;

public class SchedulingController {
    private final SchedulingService schedulingService = new SchedulingService();

    public Meeting createMeeting(Set<Long> personIds, LocalDateTime timeslot) {
        if (personIds == null || timeslot == null) {
            throw new IllegalArgumentException("Error: Please provide not null arguments");
        }

        return this.schedulingService.createMeeting(personIds, timeslot);
    }

    public Schedule getPersonSchedule(Long personId) {
        if (personId == null) {
            throw new IllegalArgumentException("Error: Please provide not null arguments");
        }

        return schedulingService.getPersonSchedule(personId);
    }

    public PersonsTimeslots suggestAvailableTimeslots(Set<Long> personIds, int availableTimeslotsCount) {
        if (personIds == null || availableTimeslotsCount <= 0) {
            throw new IllegalArgumentException("Error: Please provide not null and correct arguments");
        }

        return schedulingService.suggestAvailableTimeslots(personIds, availableTimeslotsCount);
    }
}
