import org.voiddat.controller.PersonController
import org.voiddat.model.Person
import org.voiddat.repository.PersonRepository
import spock.lang.Shared
import spock.lang.Specification

import java.sql.SQLIntegrityConstraintViolationException

class PersonControllerIntegrationTest extends Specification {

    @Shared PersonController personController
    @Shared String personName

    def setupSpec() {
        personController = new PersonController()
        personName = 'Smith'
        PersonRepository.PERSONS.clear()
    }

    def cleanupSpec() {
        PersonRepository.PERSONS.clear()
    }

    def 'should create a person'() {
        given:
        String email = 'tested@example.com'

        when:
        personController.addPerson(personName, email)

        then:
        personController.getPerson(1L).get() == new Person(1L, personName, email, new HashSet<>())
    }

    def 'should throw an exception - incorrect email'() {
        given:
        String incorrectEmail = 'testexample.com'

        when:
        personController.addPerson(personName, incorrectEmail)

        then:
        thrown(IllegalArgumentException)
    }

    def 'should throw an exception - duplicate email'() {
        given:
        String email = 'test@example.com'

        when:
        personController.addPerson(personName, email)
        personController.addPerson(personName, email)

        then:
        thrown(SQLIntegrityConstraintViolationException)
    }

    def 'addPerson should throw IllegalArgumentException - passed null email'() {
        when:
        personController.addPerson(personName, null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getPerson should throw IllegalArgumentException - passed null personId'() {
        when:
        personController.getPerson(null)

        then:
        thrown(IllegalArgumentException)
    }
}
