package com.example;

import com.example.entity.Gender;
import com.example.entity.Person;
import com.example.repository.PersonRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class TestProjectApplication {
  private final PersonRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(TestProjectApplication.class, args);
	}

  /**
   * Generate test data and insert in into database
   */
  @PostConstruct
  public void init() {
    if(repository.count() == 0) {
      for (int i = 0; i < 100; i++) {
        Person person = new Person();
        person.setId("000000-111"+i);
        person.setFirstName("Name "+i);
        person.setLastName("Surname "+i);

        if(i%3 == 0)
          person.setGender(Gender.MALE);
        else
          person.setGender(Gender.FEMALE);

        long minimalDate = LocalDate.of(1990, 1, 1).getLong(ChronoField.EPOCH_DAY);
        long maximalDate = LocalDate.of(2000,1,1).getLong(ChronoField.EPOCH_DAY);
        long generatedDate = ThreadLocalRandom.current().longs(minimalDate, maximalDate).limit(1).findFirst().orElse(0);
        person.setDateOfBirth(LocalDate.ofEpochDay(generatedDate));

        repository.save(person);
      }
    }
  }
}
