package com.example.service;

import com.example.entity.Gender;
import com.example.entity.Person;
import com.example.repository.PersonRepository;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PersonServiceUnitTest {
  private final PersonRepository repository = Mockito.mock(PersonRepository.class);
  private final PersonService service = new PersonService(repository);

  @Test
  void findPersonFilledList() {
    Mockito.doReturn(generateTestPersons()).when(repository).findAll();
    Assertions.assertFalse(service.getPersonList().isEmpty());
  }

  @Test
  void findPersonEmptyList() {
    Mockito.doReturn(List.of()).when(repository).findAll();
    Assertions.assertTrue(service.getPersonList().isEmpty());
  }

  @Test
  void findExistingPersonById() {
    Optional<Person> personMockData = Optional.of(generateTestPersons().get(0));
    Mockito.doReturn(personMockData).when(repository).findById("000000-00000");
    Person personResult = service.findPersonById("000000-00000");

    Assertions.assertNotNull(personResult);
    Assertions.assertEquals("First name0", personResult.getFirstName());
    Assertions.assertEquals("Last name0", personResult.getLastName());
    Assertions.assertEquals(Gender.MALE, personResult.getGender());
    Assertions.assertEquals(LocalDate.of(1990, Month.JULY, 5), personResult.getDateOfBirth());
  }

  @Test
  void findNotExistingPersonById() {
    Optional<Person> personMockData = Optional.of(generateTestPersons().get(0));
    Mockito.doReturn(personMockData).when(repository).findById("000000-00000");
    Assertions.assertThrows(NoSuchElementException.class, () -> {
      service.findPersonById("000000-00065");
    });
  }

  @Test
  void findExistingPersonByIdAndBirthDate() {
    Mockito.doReturn(Optional.of(generateTestPersons().get(2)))
        .when(repository)
        .findByIdAndDateOfBirth("000000-00001", LocalDate.of(1992, Month.JANUARY, 8));
    Person personResult = service.findByIdAndDateOfBirth("000000-00001",
                                  LocalDate.of(1992, Month.JANUARY, 8));

    Assertions.assertNotNull(personResult);
    Assertions.assertEquals("First name2", personResult.getFirstName());
    Assertions.assertEquals("Last name2", personResult.getLastName());
    Assertions.assertEquals(Gender.FEMALE, personResult.getGender());
    Assertions.assertEquals(LocalDate.of(1992, Month.JANUARY, 8), personResult.getDateOfBirth());
  }

  @Test
  void findNotExistingPersonByIdAndBirthDate() {
    Mockito.doReturn(Optional.of(generateTestPersons().get(2)))
        .when(repository)
        .findByIdAndDateOfBirth("000000-00001", LocalDate.of(1992, Month.JANUARY, 8));
    LocalDate dateToTest = LocalDate.of(1992, Month.JANUARY, 9);

    Assertions.assertThrows(NoSuchElementException.class, () -> {
      service.findByIdAndDateOfBirth("000000-00034", dateToTest);
    });
  }

  @Test
  void findPersonsByBirthDate() {
    List<Person> personMockData = List.of(generateTestPersons().get(0), generateTestPersons().get(1));
    Mockito.doReturn(personMockData).when(repository).findByDateOfBirth(LocalDate.of(1990, Month.JULY, 5));
    Assertions.assertFalse(service.findByDateOfBirth(LocalDate.of(1990, Month.JULY, 5)).isEmpty());
  }

  @Test
  void didNotFindPersonsByBirthDate() {
    Mockito.doReturn(List.of()).when(repository).findByDateOfBirth(LocalDate.of(1990, Month.JULY, 5));
    Assertions.assertTrue(service.findByDateOfBirth(LocalDate.of(1990, Month.JULY, 5)).isEmpty());
  }

  @Test
  void addValidPerson() {
    Person validPerson = new Person();
    validPerson.setId("000000-11111");
    validPerson.setLastName("Valid surname");
    validPerson.setFirstName("Valid name");
    validPerson.setGender(Gender.MALE);
    validPerson.setDateOfBirth(LocalDate.now());

    service.addPerson(validPerson);
    Mockito.verify(repository, Mockito.times(1)).save(validPerson);
  }

  @Test
  void addInvalidPerson() {
    Person invalidPerson = new Person();
    invalidPerson.setId("000000-11111");
    invalidPerson.setGender(Gender.MALE);
    invalidPerson.setLastName("");
    invalidPerson.setFirstName("");
    invalidPerson.setDateOfBirth(LocalDate.now());

    Assertions.assertThrows(RuntimeException.class, () -> {
      service.addPerson(invalidPerson);
    });
  }

  @Test
  void updateValidPerson() {
    Person updatedPerson = new Person();
    updatedPerson.setId("someFakeId");
    updatedPerson.setLastName("New surname");
    updatedPerson.setFirstName("New name");
    updatedPerson.setDateOfBirth(LocalDate.now());
    service.updatePerson(updatedPerson, generateTestDatabasePerson());
    Mockito.verify(repository, Mockito.times(1)).save(updatedPerson);
  }

  @Test
  void updateInvalidPerson() {
    Person updatedPerson = new Person();
    updatedPerson.setId("someFakeId");
    updatedPerson.setLastName("");
    updatedPerson.setFirstName("");
    updatedPerson.setDateOfBirth(LocalDate.now());

    Person personFromDB = generateTestDatabasePerson();
    Assertions.assertThrows(RuntimeException.class, () -> {
      service.updatePerson(updatedPerson, personFromDB);
    });
  }

  @Test
  void deleteExistingPerson() {
    Optional<Person> personMockData = Optional.of(generateTestDatabasePerson());
    Mockito.doReturn(personMockData).when(repository).findById("111111-11111");
    service.deletePerson(generateTestDatabasePerson());
    Mockito.verify(repository, Mockito.times(1)).delete(generateTestDatabasePerson());
  }

  @Test
  void deleteNotExistingPerson() {
    Person notExistingPerson = new Person();
    Assertions.assertThrows(NoSuchElementException.class, () -> {
      service.deletePerson(notExistingPerson);
    });
  }

  private List<Person> generateTestPersons() {
    List<Person> personList = new ArrayList<>();

    for (int i = 0; i < 3; i++) {
      Person person = new Person();
      person.setId("000000-0000"+i);
      person.setFirstName("First name"+i);
      person.setLastName("Last name"+i);

      if (i < 2) {
        person.setGender(Gender.MALE);
        person.setDateOfBirth(LocalDate.of(1990, Month.JULY, 5));
      } else {
        person.setGender(Gender.FEMALE);
        person.setDateOfBirth(LocalDate.of(1992, Month.JANUARY, 8));
      }

      personList.add(person);
    }

    return personList;
  }

  private Person generateTestDatabasePerson() {
    Person personFromDB = new Person();

    personFromDB.setId("111111-11111");
    personFromDB.setGender(Gender.MALE);
    personFromDB.setFirstName("Old name");
    personFromDB.setLastName("Old surname");
    personFromDB.setDateOfBirth(LocalDate.now());

    return personFromDB;
  }
}