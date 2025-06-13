package com.springboot.study.repository;

import com.springboot.study.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Person entity with additional search methods
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    
    /**
     * Find persons by first name (case-insensitive, partial match)
     */
    List<Person> findByFirstNameContainingIgnoreCase(String firstName);
}