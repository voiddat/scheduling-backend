package org.voiddat.controller;

import org.voiddat.model.Person;
import org.voiddat.service.PersonService;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

public class PersonController {
    private final PersonService personService = new PersonService();

    public Person addPerson(String name, String email) throws SQLIntegrityConstraintViolationException {
        if(name == null || email == null) {
            throw new IllegalArgumentException("Error: Please provide not null arguments");
        }
        // the SQLIntegrityConstraintViolationException exception could also be handled here in order to let client know about the error (generic message like HTTP 500)

        return personService.addPerson(new Person(name, email));
    }

    public Optional<Person> getPerson(Long personId) {
        if(personId == null) {
            throw new IllegalArgumentException("Error: Please provide not null arguments");
        }

        return this.personService.getPerson(personId);
    }
}
