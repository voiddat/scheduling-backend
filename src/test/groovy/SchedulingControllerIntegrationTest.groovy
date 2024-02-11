import org.voiddat.controller.PersonController
import org.voiddat.controller.SchedulingController
import org.voiddat.exception.UnavailableTimeslotException
import org.voiddat.model.Meeting
import org.voiddat.model.Person
import org.voiddat.model.PersonsTimeslots
import org.voiddat.model.Schedule
import org.voiddat.repository.MeetingRepository
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDateTime

class SchedulingControllerIntegrationTest extends Specification {
    @Shared private PersonController personController
    @Shared private SchedulingController schedulingController
    @Shared private Person person1
    @Shared private Person person2
    @Shared private Person person3
    @Shared private LocalDateTime timeslot = LocalDateTime.now()
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

    def setupSpec() {
        personController = new PersonController()
        schedulingController = new SchedulingController()
        person1 = personController.addPerson('Smith', 'test@example.com')
        person2 = personController.addPerson('West', 'test2@example.com')
        person3 = personController.addPerson('Jackson', 'test3@example.com')
    }

    def cleanup() {
        MeetingRepository.MEETINGS.clear()
    }

    def 'should create a meeting'() {
        when:
        Meeting meeting = schedulingController.createMeeting(Set.of(person1.id(), person2.id(), person3.id()), timeslot)

        then:
        meeting.id() == 1L &&
                meeting.timeslot() == timeslot &&
                meeting.persons() == Set.of(person1, person2, person3)
    }

    def 'should trigger a conflict - tries to create two meeting for one person at the same time'() {
        given:
        schedulingController.createMeeting(Set.of(person1.id(), person2.id()), timeslot)

        when:
        schedulingController.createMeeting(Set.of(person2.id()), timeslot)

        then:
        thrown(UnavailableTimeslotException)
    }

    def 'should get person schedule'() {
        given:
        Meeting meeting1 = schedulingController.createMeeting(Set.of(person1.id(), person2.id()), timeslot.plusHours(1))
        Meeting meeting2 = schedulingController.createMeeting(Set.of(person1.id()), timeslot.plusHours(2))

        when:
        Schedule schedule1 = schedulingController.getPersonSchedule(person1.id())
        Schedule schedule2 = schedulingController.getPersonSchedule(person2.id())

        then:
        schedule1.person() == person1 && schedule1.meetings() == List.of(meeting1, meeting2)
        and:
        schedule2.person() == person2 && schedule2.meetings() == List.of(meeting1)
    }

    def 'should suggest first 5 available timeslots for given persons'() {
        given:
        schedulingController.createMeeting(Set.of(person1.id(), person2.id()), timeslot.plusHours(1))
        schedulingController.createMeeting(Set.of(person1.id()), timeslot.plusHours(2))
        schedulingController.createMeeting(Set.of(person3.id()), timeslot.plusHours(3))

        when:
        PersonsTimeslots personsTimeslots = schedulingController.suggestAvailableTimeslots(Set.of(person1.id(), person2.id(), person3.id()), 5)

        then:
        personsTimeslots == new PersonsTimeslots(
                Set.of(person1, person2, person3),
                List.of(
                        timeslot.plusHours(4),
                        timeslot.plusHours(5),
                        timeslot.plusHours(6),
                        timeslot.plusHours(7),
                        timeslot.plusHours(8)
                )
        )

    }

    def 'createMeeting  should throw IllegalArgumentException - passed null timeslot'() {
        when:
        schedulingController.createMeeting(Set.of(person1.id()), null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'createMeeting should throw IllegalArgumentException - passed null personIds'() {
        when:
        schedulingController.createMeeting(null, timeslot)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getPersonSchedule should throw IllegalArgumentException - passed null personId'() {
        when:
        schedulingController.getPersonSchedule(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'suggestAvailableTimeslots should throw IllegalArgumentException - passed null personIds'() {
        when:
        schedulingController.suggestAvailableTimeslots(null, 1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'suggestAvailableTimeslots should throw IllegalArgumentException - passed negative availableTimeslotsCount'() {
        when:
        schedulingController.suggestAvailableTimeslots(Set.of(person1.id()), -1)

        then:
        thrown(IllegalArgumentException)
    }
}
