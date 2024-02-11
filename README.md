# Scheduling app

## How to run

### Prerequisites
* Java 17
* JAVA_HOME environmental variable set properly

### Unix-based OS
```shell
./gradlew test
```

### Windows
```shell
./gradlew.bat test
```

## Task definition

Design and implement the API for a minimal scheduling application. This API should be able to handle the following requirements:

1) Create persons with a name and unique email.
2) Create meetings involving one or more persons at a given time slot.
3) A meeting can only start at the hour mark and only last exactly one hour.
4) Show the schedule, i.e., the upcoming meetings, for a given person.
5) Suggest one or more available timeslots for meetings given a group of persons.

You should not implement a GUI, a simple unit test or console demo
should suffice. Also, please try to keep the number of third party
libraries to a minimum. Finally, please do not spend time on storing
data in files or databases.

The exercise should be solved in Java or TypeScript.

The estimated work effort for this exercise is 2-3 hours. If you make any implementation compromises, please make sure to document those. You will be evaluated both on your coding style and your ability to make the right implementation compromises.

## Analysis

An app should properly schedule meetings for one or more persons at given time slots. It's necessary to implement a mechanism that will prevent conflicting i.e. a person can't have 2 meetings in one time slot.
I think it's necessary to validate all fields and existence of all entities necessary to perform certain business logic (like I need existing person to assign it to a meeting). 

In real world it would also be necessary to consider different timezones for each client and suggest reasonable (not in the middle of the night) time if possible.



## API

1) **Create** **persons** with a **name** and **unique email**.<br>
```Person addPerson(String name, String email)```
2) **Create** **meetings** involving **one or more persons** at a **given time slot**.<br>
```Meeting createMeeting(Set<Long> personIds, LocalDateTime timeslot)```
3) **Show** the **schedule**, i.e., the upcoming meetings, **for a given person**.<br>
```Schedule getPersonSchedule(Long personId)```
4) **Suggest** **one or more available timeslots** for **meetings** given a **group of persons**.<br>
```PersonsTimeslots suggestAvailableTimeslots(Set<Long> personIds, int availableTimeslotsCount)```
