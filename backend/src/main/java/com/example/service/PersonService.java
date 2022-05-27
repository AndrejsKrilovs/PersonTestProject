package com.example.service;

import com.example.entity.Person;
import com.example.repository.PersonRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {
  private final PersonRepository repository;

  public List<Person> getPersonList() {
    log.info("Finding all persons .....");
    List<Person> result = repository.findAll();
    log.info("Found {} persons", result.size());
    return result;
  }

  public Person findPersonById(String personIdentifier) {
    log.info("Finding person by personIdentifier={} .....", personIdentifier);
    Person result = repository.findById(personIdentifier).orElseThrow();
    log.info("Found person {}", result);
    return result;
  }

  public Person findByIdAndDateOfBirth(String personIdentifier, LocalDate personBirthDate) {
    log.info("Finding person by personIdentifier={} and birthDate={} .....", personIdentifier, personBirthDate);
    Person result = repository.findByIdAndDateOfBirth(personIdentifier, personBirthDate)
                              .orElseThrow(NoSuchElementException::new);
    log.info("Found person {}", result);
    return result;
  }

  public List<Person> findByDateOfBirth(LocalDate personBirthDate) {
    log.info("Finding persons by birthDate={} .....", personBirthDate);
    List<Person> result = repository.findByDateOfBirth(personBirthDate);
    log.info("Found {} persons", result.size());
    return result;
  }

  public Person addPerson(Person newPerson) {
    if(newPerson.getFirstName().isBlank())
      throw new RuntimeException("Person name cannot be empty");

    if(newPerson.getLastName().isBlank())
      throw new RuntimeException("Person surname cannot be empty");

    if(newPerson.getDateOfBirth().toString().isBlank())
      throw new RuntimeException("Person birth date cannot be empty");

    if(newPerson.getDateOfBirth().compareTo(LocalDate.now()) > 0)
      throw new RuntimeException("Birth date should be valid");

    if(getPersonList().contains(newPerson)) {
      throw new RuntimeException("Person exists in database");
    } else {
      log.info("Adding new person .....");
      Person validPerson = repository.save(newPerson);
      log.info("Person {} added", validPerson);
      return validPerson;
    }
  }

  public Person updatePerson(Person newPerson, Person personFromDB) {
    if(newPerson.getFirstName().isBlank())
      throw new RuntimeException("Person name cannot be empty");

    if(newPerson.getLastName().isBlank())
      throw new RuntimeException("Person surname cannot be empty");

    if(newPerson.getDateOfBirth().toString().isBlank())
      throw new RuntimeException("Person birth date cannot be empty");

    if(newPerson.getDateOfBirth().compareTo(LocalDate.now()) > 0)
      throw new RuntimeException("Birth date should be valid");

    log.info("Updating person {} .....", personFromDB);
    newPerson.setId(personFromDB.getId());
    BeanUtils.copyProperties(newPerson, personFromDB, "id");
    Person result = repository.save(personFromDB);
    log.info("Person with id={} updated to {}", personFromDB.getId(), personFromDB);
    return result;
  }

  public void deletePerson(Person personToDelete) {
    log.info("Deleting person {} .....", personToDelete);
    repository.findById(personToDelete.getId()).ifPresentOrElse(item -> {
      repository.delete(item);
      log.info("Person with id={} deleted", personToDelete.getId());
    }, () -> {
      throw new NoSuchElementException("Person for delete not found");
    });
  }
}
