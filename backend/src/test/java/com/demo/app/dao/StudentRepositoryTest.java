package com.demo.app.dao;

import com.demo.app.AbstractTestContainers;
import com.demo.app.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentRepositoryTest extends AbstractTestContainers {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        System.out.println("Bean count: " + applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsStudentByEmail() {

        String email = FAKER.internet().safeEmailAddress();
        Student student = new Student(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(21, 67)
        );

        studentRepository.save(student);

        boolean actual = studentRepository.existsStudentByEmail(email);
        assertThat(actual).isTrue();

    }

}