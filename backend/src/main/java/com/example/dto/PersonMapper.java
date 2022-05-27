package com.example.dto;

import com.example.entity.Person;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class PersonMapper {
  public PersonDTO toDTO(Person entity) {
    return PersonDTO.builder()
        .id(entity.getId())
        .firstName(entity.getFirstName())
        .lastName(entity.getLastName())
        .gender(entity.getGender())
        .dateOfBirth(entity.getDateOfBirth())
        .build();
  }

  public Person toEntity(PersonDTO dto) {
    Person person = new Person();
    person.setId(dto.getId());
    person.setFirstName(dto.getFirstName());
    person.setLastName(dto.getLastName());
    person.setGender(dto.getGender());
    person.setDateOfBirth(dto.getDateOfBirth());
    return person;
  }
}
