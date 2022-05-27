package com.example.repository;

import com.example.entity.Person;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
  Optional<Person> findByIdAndDateOfBirth(String id, LocalDate dateOfBirth);
  List<Person> findByDateOfBirth(LocalDate dateOfBirth);
}
