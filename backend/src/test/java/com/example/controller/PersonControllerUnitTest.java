package com.example.controller;

import com.example.dto.PersonDTO;
import com.example.dto.PersonMapper;
import com.example.entity.Gender;
import com.example.entity.Person;
import com.example.service.PersonService;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PersonControllerUnitTest {
  private final PersonService service = Mockito.mock(PersonService.class);
  private final PersonMapper mapper = Mockito.mock(PersonMapper.class);
  private final PersonController controller = new PersonController(service, mapper);

  @Test
  void showAllPersons() {
    Mockito.doReturn(generateTestPersons()).when(service).getPersonList();
    Assertions.assertFalse(controller.showAllPersons().isEmpty());
  }

  @Test
  void showPersonsEmptyList() {
    Mockito.doReturn(List.of()).when(service).getPersonList();
    Assertions.assertTrue(controller.showAllPersons().isEmpty());
  }

  @Test
  void findExistingPersonById() {
    Mockito.doReturn(generateTestPersons().get(0))
        .when(service).findPersonById("000000-00000");
    Mockito.doReturn(generateTestDTOs().get(0))
        .when(mapper).toDTO(generateTestPersons().get(0));
    PersonDTO personResult = controller.findPersonById("000000-00000");

    Assertions.assertNotNull(personResult);
    Assertions.assertEquals("First name0", personResult.getFirstName());
    Assertions.assertEquals("Last name0", personResult.getLastName());
    Assertions.assertEquals(Gender.MALE, personResult.getGender());
    Assertions.assertEquals(LocalDate.of(1990, Month.JULY, 5), personResult.getDateOfBirth());
  }

  @Test
  void findNotExistingPersonById() {
    Mockito.doReturn(generateTestPersons().get(0))
        .when(service).findPersonById("000000-00000");
    Mockito.doReturn(generateTestDTOs().get(0))
        .when(mapper).toDTO(generateTestPersons().get(0));

    Assertions.assertThrows(NoSuchElementException.class, () -> {
      controller.findPersonById("000000-00065");
    });
  }

  @Test
  void findExistingPersonByIdAndBirthDate() {
    Mockito.doReturn(generateTestPersons().get(2))
        .when(service).findByIdAndDateOfBirth("000000-00001", LocalDate.of(1992, Month.JANUARY, 8));
    Mockito.doReturn(generateTestDTOs().get(2))
        .when(mapper).toDTO(generateTestPersons().get(2));
    PersonDTO personResult = controller.findPersonsByIdAndBirthDate("000000-00001", "1992-01-08");

    Assertions.assertNotNull(personResult);
    Assertions.assertEquals("First name2", personResult.getFirstName());
    Assertions.assertEquals("Last name2", personResult.getLastName());
    Assertions.assertEquals(Gender.FEMALE, personResult.getGender());
    Assertions.assertEquals(LocalDate.of(1992, Month.JANUARY, 8), personResult.getDateOfBirth());
  }

  @Test
  void findNotExistingPersonByIdAndBirthDate() {
    Mockito.doReturn(generateTestPersons().get(2))
        .when(service).findByIdAndDateOfBirth("000000-00001", LocalDate.of(1992, Month.JANUARY, 8));
    Mockito.doReturn(generateTestDTOs().get(2))
        .when(mapper).toDTO(generateTestPersons().get(2));

    Assertions.assertThrows(NoSuchElementException.class, () -> {
      controller.findPersonsByIdAndBirthDate("000000-00065", "1992-01-08");
    });
  }

  @Test
  void findPersonsByBirthDate() {
    List<Person> personMockData = List.of(generateTestPersons().get(0), generateTestPersons().get(1));
    Mockito.doReturn(personMockData)
        .when(service).findByDateOfBirth(LocalDate.of(1990, Month.JULY, 5));
    Mockito.doReturn(generateTestDTOs().get(0))
        .when(mapper).toDTO(generateTestPersons().get(0));
    Mockito.doReturn(generateTestDTOs().get(1))
        .when(mapper).toDTO(generateTestPersons().get(1));

    List<PersonDTO> result = controller.findPersonsByBirthDate("1990-07-05");
    Assertions.assertFalse(result.isEmpty());
    Assertions.assertEquals(2, result.size());
  }

  @Test
  void addValidPerson() {
    Person validPerson = new Person();
    validPerson.setId("000000-11111");
    validPerson.setLastName("Valid surname");
    validPerson.setFirstName("Valid name");
    validPerson.setGender(Gender.MALE);
    validPerson.setDateOfBirth(LocalDate.now());

    PersonDTO personDto = PersonDTO.builder()
        .id("000000-11111")
        .firstName("Valid name").lastName("Valid surname")
        .gender(Gender.MALE)
        .dateOfBirth(LocalDate.now())
        .build();

    Mockito.doReturn(validPerson)
        .when(service).addPerson(validPerson);
    Mockito.doReturn(validPerson)
        .when(mapper).toEntity(personDto);

    Person result = controller.addNewPerson(personDto);
    Assertions.assertNotNull(result);
    Assertions.assertEquals("000000-11111", result.getId());
  }

  @Test
  void updateValidPerson() {
    Person personFromDB = new Person();
    personFromDB.setId("000000-11111");
    personFromDB.setLastName("Valid surname");
    personFromDB.setFirstName("Valid name");
    personFromDB.setGender(Gender.MALE);
    personFromDB.setDateOfBirth(LocalDate.now());

    Person updatedPerson = new Person();
    updatedPerson.setId("000000-11111");
    updatedPerson.setLastName("Updated surname");
    updatedPerson.setFirstName("Updated name");
    updatedPerson.setGender(Gender.FEMALE);
    updatedPerson.setDateOfBirth(LocalDate.now().minusMonths(2L));

    PersonDTO updatedDTO = PersonDTO.builder()
        .id("some-fake-id")
        .firstName("Updated name").lastName("Updated surname")
        .gender(Gender.FEMALE)
        .dateOfBirth(LocalDate.now().minusMonths(2L))
        .build();

    Mockito.doReturn(updatedPerson)
        .when(mapper).toEntity(updatedDTO);
    Mockito.doReturn(updatedPerson)
        .when(service).updatePerson(updatedPerson, personFromDB);

    Person result = controller.updatePerson(personFromDB, updatedDTO);
    Assertions.assertNotNull(result);
    Assertions.assertEquals("000000-11111", result.getId());
    Assertions.assertEquals("Updated name", result.getFirstName());
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

  private List<PersonDTO> generateTestDTOs() {
    List<PersonDTO> personList = new ArrayList<>();

    for (int i = 0; i < 3; i++) {
      PersonDTO person = PersonDTO.builder()
          .id("000000-0000"+i)
          .firstName("First name"+i)
          .lastName("Last name"+i)
          .gender(i < 2 ? Gender.MALE : Gender.FEMALE)
          .dateOfBirth(i < 2 ? LocalDate.of(1990, Month.JULY, 5) : LocalDate.of(1992, Month.JANUARY, 8))
          .build();

      personList.add(person);
    }

    return personList;
  }

}