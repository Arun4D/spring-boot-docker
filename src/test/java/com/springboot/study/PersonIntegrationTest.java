package com.springboot.study;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.springboot.study.domain.Person;
import com.springboot.study.repository.PersonRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
class PersonIntegrationTest {

    private WebTestClient webTestClient;

    @org.springframework.boot.test.web.server.LocalServerPort
    private int port;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
        personRepository.deleteAll();
    }

    @Test
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
        webTestClient.get().uri("/api/persons")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(3)
                .jsonPath("$[0].firstName").isEqualTo("John")
                .jsonPath("$[1].firstName").isEqualTo("Jane")
                .jsonPath("$[2].firstName").isEqualTo("Alice");
    }

    @Test
    void shouldSearchPersonsByFirstName() throws Exception {
        // given
        createTestPerson("John", "Doe");
        createTestPerson("Johnny", "Smith");
        createTestPerson("Alice", "Johnson");

        // when & then
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/api/persons/search")
                .queryParam("firstName", "John")
                .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].firstName").isEqualTo("John")
                .jsonPath("$[1].firstName").isEqualTo("Johnny");
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchingFirstName() throws Exception {
        // given
        createTestPerson("John", "Doe");
        createTestPerson("Jane", "Smith");

        // when & then
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/api/persons/search")
                .queryParam("firstName", "XYZ")
                .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(0);
    }

    @Test
    void shouldReturnBadRequestWhenFirstNameIsEmpty() throws Exception {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/api/persons/search")
                .queryParam("firstName", "")
                .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    private Person createTestPerson(String firstName, String lastName) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        return personRepository.save(person);
    }
}