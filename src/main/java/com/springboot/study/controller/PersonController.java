package com.springboot.study.controller;

import com.springboot.study.assembler.PersonAssembler;
import com.springboot.study.domain.Person;
import com.springboot.study.dto.PersonDTO;
import com.springboot.study.repository.PersonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Person operations
 */
@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonRepository personRepository;
    private final PersonAssembler personAssembler;

    public PersonController(PersonRepository personRepository, PersonAssembler personAssembler) {
        this.personRepository = personRepository;
        this.personAssembler = personAssembler;
    }

    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(
            @RequestParam(defaultValue = "defaultFirstName") String firstName,
            @RequestParam(defaultValue = "defaultLastName") String lastName) {
        
        Person person = personAssembler.toPerson(firstName, lastName);
        Person savedPerson = personRepository.save(person);
        PersonDTO personDTO = personAssembler.toPersonDTO(savedPerson);
        
        return ResponseEntity.ok(personDTO);
    }    
    
    @GetMapping
    public ResponseEntity<List<PersonDTO>> getAllPersons() {
        return ResponseEntity.ok(
            personRepository.findAll()
                .stream()
                .map(personAssembler::toPersonDTO)
                .toList()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<PersonDTO>> getByFirstName(
            @RequestParam(required = true) String firstName) {
        
        if (firstName == null || firstName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
            personRepository.findByFirstNameContainingIgnoreCase(firstName)
                .stream()
                .map(personAssembler::toPersonDTO)
                .toList()
        );
    }
}
