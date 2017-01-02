package com.springboot.study.assembler;

import com.springboot.study.domain.Person;
import com.springboot.study.dto.PersonDTO;
import org.springframework.stereotype.Component;

/**
 * Created by aduraisamy on 1/2/2017.
 */
@Component
public class PersonAssembler {
    public Person toPerson(String firstName, String lastName) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        return person;
    }

    public PersonDTO toPersonDTO(Person person) {
        PersonDTO personDTO = new PersonDTO(person.getId(), person.getFirstName(), person.getLastName());
        return personDTO;
    }
}
