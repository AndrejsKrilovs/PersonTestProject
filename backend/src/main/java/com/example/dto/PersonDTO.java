package com.example.dto;

import com.example.entity.Gender;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDate;

@Value
@Builder
public class PersonDTO {
  String id;
  String firstName;
  String lastName;
  Gender gender;
  LocalDate dateOfBirth;
}
