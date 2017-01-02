package com.springboot.study.repository;

import com.springboot.study.domain.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by aduraisamy on 1/2/2017.
 */
@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

}