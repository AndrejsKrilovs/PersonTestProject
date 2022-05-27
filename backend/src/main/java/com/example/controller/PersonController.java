package com.example.controller;

import com.example.dto.PersonDTO;
import com.example.dto.PersonMapper;
import com.example.entity.Person;
import com.example.service.PersonService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/person.svc")
@CrossOrigin(origins = "http://localhost:4200")
public class PersonController {
  private final PersonService service;
  private final PersonMapper mapper;

  @GetMapping(path = "/Persons")
  public List<PersonDTO> showAllPersons() {
    return service.getPersonList()
        .stream()
        .map(mapper::toDTO)
        .collect(Collectors.toList());
  }

  @GetMapping(path = "/Person(k={id})")
  public PersonDTO findPersonById(@PathVariable String id) {
    return Optional.ofNullable(mapper.toDTO(service.findPersonById(id))).orElseThrow();
  }

  @GetMapping(path = "/Person(dt={birthDate})")
  public List<PersonDTO> findPersonsByBirthDate(@PathVariable String birthDate) {
    LocalDate parsedDate = LocalDate.parse(birthDate, DateTimeFormatter.ISO_DATE);
    return service.findByDateOfBirth(parsedDate)
        .stream()
        .map(mapper::toDTO)
        .collect(Collectors.toList());
  }

  @GetMapping(path = "/Person()")
  public PersonDTO findPersonsByIdAndBirthDate(@RequestParam String id, @RequestParam("dt") String birthDate) {
    LocalDate parsedDate = LocalDate.parse(birthDate, DateTimeFormatter.ISO_DATE);
    return Optional.ofNullable(mapper.toDTO(service.findByIdAndDateOfBirth(id, parsedDate))).orElseThrow();
  }

  @PostMapping
  public Person addNewPerson(@RequestBody PersonDTO person) {
    return Optional.ofNullable(service.addPerson(mapper.toEntity(person))).orElseThrow();
  }

  @PutMapping(path = "/Person(k={id})")
  public Person updatePerson(@PathVariable Person id, @RequestBody PersonDTO updatedPerson) {
    return service.updatePerson(mapper.toEntity(updatedPerson), id);
  }

  @DeleteMapping(path = "/Person(k={id})")
  public void deletePerson(@PathVariable Person id) {
    service.deletePerson(id);
  }
}
