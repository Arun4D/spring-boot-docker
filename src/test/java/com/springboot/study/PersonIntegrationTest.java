package com.springboot.study;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.springboot.study.domain.Person;
import com.springboot.study.repository.PersonRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
class PersonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;


    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
    }    @Test
    void shouldSaveAndRetrievePerson() {
        // given
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");

        // when
        Person savedPerson = personRepository.save(person);

        // then
        assertNotNull(savedPerson.getId());
        Person foundPerson = personRepository.findById(savedPerson.getId()).orElseThrow();
        assertEquals("John", foundPerson.getFirstName());
        assertEquals("Doe", foundPerson.getLastName());
    }

    @Test
    void shouldGetAllPersons() throws Exception {
        // given
        createTestPerson("John", "Doe");
        createTestPerson("Jane", "Smith");
        createTestPerson("Alice", "Johnson");

        // when & then
        mockMvc.perform(get("/api/persons"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("Jane")))
                .andExpect(jsonPath("$[2].firstName", is("Alice")));
    }

    @Test
    void shouldSearchPersonsByFirstName() throws Exception {
        // given
        createTestPerson("John", "Doe");
        createTestPerson("Johnny", "Smith");
        createTestPerson("Alice", "Johnson");

        // when & then
        mockMvc.perform(get("/api/persons/search")
                    .param("firstName", "John"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("Johnny")));
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchingFirstName() throws Exception {
        // given
        createTestPerson("John", "Doe");
        createTestPerson("Jane", "Smith");

        // when & then
        mockMvc.perform(get("/api/persons/search")
                    .param("firstName", "XYZ"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldReturnBadRequestWhenFirstNameIsEmpty() throws Exception {
        mockMvc.perform(get("/api/persons/search")
                    .param("firstName", ""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private Person createTestPerson(String firstName, String lastName) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        return personRepository.save(person);
    }
}