package org.voiddat.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

public class Person {
    private final long id;
    private final String name;
    private final String email;
    private final Set<Long> meetingIds;
    private static final Pattern EMAIL_REGEXP = Pattern.compile("^.+@.+\\..+$");
    private static final AtomicLong AUTO_INCREMENT = new AtomicLong(0L);

    public Person(String name, String email) {
        if (!EMAIL_REGEXP.matcher(email).matches()) {
            throw new IllegalArgumentException("Error: Please provide correct email");
        }

        this.id = AUTO_INCREMENT.incrementAndGet();
        this.name = name;
        this.email = email;
        this.meetingIds = new HashSet<>();
    }

    public Person(long id, String name, String email, Set<Long> meetingIds) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.meetingIds = meetingIds;
    }

    public long id() {
        return id;
    }

    public String email() {
        return email;
    }

    public Set<Long> meetingIds() {
        return meetingIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && Objects.equals(email, person.email) && Objects.equals(meetingIds, person.meetingIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, meetingIds);
    }
}
