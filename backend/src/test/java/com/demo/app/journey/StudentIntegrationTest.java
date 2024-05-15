package com.demo.app.journey;

import com.demo.app.dto.StudentDataUpdate;
import com.demo.app.dto.StudentRegistrationData;
import com.demo.app.entity.Student;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class StudentIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    private static final String STUDENT_URI = "/api/v1/students";

    @Test
    void canRegisterAStudent() {

        // create student registration data
        Faker faker = new Faker();
        Name name = faker.name();
        String studentName = name.fullName();
        String email = name.lastName() + "." + name.firstName() + "@integration_test.com";
        int age = new Random().nextInt(21, 30);
        StudentRegistrationData studentRegistrationData = new StudentRegistrationData(studentName, email, age);

        // send request to register a student
        webTestClient.post()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(studentRegistrationData), StudentRegistrationData.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all students
        List<Student> students = webTestClient.get()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Student>() {
                })
                .returnResult()
                .getResponseBody();

        Student expectedStudent = new Student(studentName, email, age);

        assertThat(students)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedStudent);

        // make sure that the student is present
        Long id = students.stream()
                        .filter(stu -> stu.getEmail().equalsIgnoreCase(email))
                                .map(stu -> stu.getId())
                                        .findFirst()
                                                .orElseThrow();
        expectedStudent.setId(id);

        webTestClient.get()
                .uri(STUDENT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Student>() {
                })
                .isEqualTo(expectedStudent);

    }

    @Test
    void canDeleteAStudent() {

        // create student registration data
        Faker faker = new Faker();
        Name name = faker.name();
        String studentName = name.fullName();
        String email = name.lastName() + "." + name.firstName() + "@integration_test.com";
        int age = new Random().nextInt(21, 30);
        StudentRegistrationData studentRegistrationData = new StudentRegistrationData(studentName, email, age);

        // send request to register a student
        webTestClient.post()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(studentRegistrationData), StudentRegistrationData.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all students
        List<Student> students = webTestClient.get()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Student>() {
                })
                .returnResult()
                .getResponseBody();

        Long id = students.stream()
                .filter(stu -> stu.getEmail().equalsIgnoreCase(email))
                .map(stu -> stu.getId())
                .findFirst()
                .orElseThrow();

        // delete student by id
        webTestClient.delete()
                .uri(STUDENT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // check student is deleted successfully
        webTestClient.get()
                .uri(STUDENT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();

    }

    @Test
    void canUpdateACustomer() {

        // create student registration data
        Faker faker = new Faker();
        Name name = faker.name();
        String studentName = name.fullName();
        String email = name.lastName() + "." + name.firstName() + "@integration_test.com";
        int age = new Random().nextInt(21, 30);
        StudentRegistrationData studentRegistrationData = new StudentRegistrationData(studentName, email, age);

        // send request to register a student
        webTestClient.post()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(studentRegistrationData), StudentRegistrationData.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all students
        List<Student> students = webTestClient.get()
                .uri(STUDENT_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Student>() {
                })
                .returnResult()
                .getResponseBody();

        Long id = students.stream()
                .filter(stu -> stu.getEmail().equalsIgnoreCase(email))
                .map(stu -> stu.getId())
                .findFirst()
                .orElseThrow();

        // prepare data to update student
        String updatedName = "updatedName";
        int updatedAge = 34;
        StudentDataUpdate studentDataUpdate = new StudentDataUpdate(updatedName, null, updatedAge);

        webTestClient.put()
                .uri(STUDENT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(studentDataUpdate), StudentDataUpdate.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get student by id and verify data
        Student expectedStudent = new Student(id, updatedName, email, updatedAge);
        webTestClient.get()
                .uri(STUDENT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Student>() {
                })
                .isEqualTo(expectedStudent);

    }

}
