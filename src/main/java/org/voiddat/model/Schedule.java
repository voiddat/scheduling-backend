package org.voiddat.model;

import java.util.List;

public record Schedule(Person person, List<Meeting> meetings) {

}
