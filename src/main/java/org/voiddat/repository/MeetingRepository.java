package org.voiddat.repository;

import org.voiddat.model.Meeting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class MeetingRepository {
    // this field is like a database for sake of simplicity
    private static final List<Meeting> MEETINGS = new ArrayList<>();

    public static Meeting saveMeeting(Meeting meeting) {
        MEETINGS.add(meeting);
        return meeting;
    }

    public static List<Meeting> getMeetings(Set<Long> meetingIds) {
        return MEETINGS.stream().filter(meeting -> meetingIds.contains(meeting.id())).toList();
    }

    public static Optional<Meeting> getMeeting(Long meetingId) {
        return MEETINGS.stream().filter(meeting -> meeting.id().equals(meetingId)).findAny();
    }
}
