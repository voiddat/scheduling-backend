package org.voiddat.service;

import org.voiddat.model.Person;
import org.voiddat.model.PersonsTimeslots;
import org.voiddat.repository.PersonRepository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

public class PersonService {
    public Person addPerson(Person person) throws SQLIntegrityConstraintViolationException {
        return PersonRepository.addPerson(person);
    }

    public Optional<Person> getPerson(Long personId) {
        return PersonRepository.getPerson(personId);
    }
}
