package com.demo.app.dao.impl;

import com.demo.app.AbstractTestContainers;
import com.demo.app.entity.Student;
import com.demo.app.jdbc.utils.StudentRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class StudentDaoJdbcImplTest extends AbstractTestContainers {

    private StudentDaoJdbcImpl studentDaoJdbcImpl;
    private final StudentRowMapper studentRowMapper = new StudentRowMapper();

    @BeforeEach
    void setUp() {
        // setting up fresh new object before executing any test
        // so that we get fresh and clean object of 'StudentDaoJdbcImpl'
        // for each test case
        // which ensures that even if any test case changes the state of
        // 'studentDaoJdbcImpl' in any way possible
        // then it won't be affecting any other test case
        studentDaoJdbcImpl = new StudentDaoJdbcImpl(getJdbcTemplate(), studentRowMapper);
    }

    @Test
    void getAllStudents() {

        Student student = new Student(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                new Random().nextInt(21, 67)
        );

        studentDaoJdbcImpl.saveStudent(student);

        List<Student> students = studentDaoJdbcImpl.getAllStudents();

        assertThat(students).isNotEmpty();

    }

    @Test
    void saveStudent() {

        String email = FAKER.internet().safeEmailAddress();
        Student student = new Student(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(21, 89)
        );

        studentDaoJdbcImpl.saveStudent(student);
        Optional<Student> actual = studentDaoJdbcImpl.getAllStudents().stream()
                .filter(stu -> stu.getEmail().equalsIgnoreCase(email))
                .findFirst();
        assertThat(actual).isNotEmpty().hasValueSatisfying(studentObj -> {
                    assertThat(studentObj.getName()).isEqualTo(student.getName());
                    assertThat(studentObj.getEmail()).isEqualTo(student.getEmail());
                    assertThat(studentObj.getAge()).isEqualTo(student.getAge());
                }

        );

    }

    @Test
    void getStudent() {

        String email = FAKER.internet().safeEmailAddress();
        Student student = new Student(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(21, 67)
        );

        studentDaoJdbcImpl.saveStudent(student);

        Long id = studentDaoJdbcImpl.getAllStudents().stream()
                .filter(s -> s.getEmail().equals(email))
                .map(stu -> stu.getId())
                .findFirst()
                .orElseThrow();

        Optional<Student> actual = studentDaoJdbcImpl.getStudent(id);
        assertThat(actual).isNotEmpty().hasValueSatisfying(studentObj -> {
            assertThat(studentObj.getName()).isEqualTo(student.getName());
            assertThat(studentObj.getEmail()).isEqualTo(student.getEmail());
            assertThat(studentObj.getAge()).isEqualTo(student.getAge());
        });

    }

    @Test
    void updateStudent() {

        String email = FAKER.internet().safeEmailAddress();
        Student student = new Student(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(21, 67)
        );

        studentDaoJdbcImpl.saveStudent(student);

        Student actual = studentDaoJdbcImpl.getAllStudents()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .findFirst()
                .orElseThrow();

        String newName = "Test Name";
        actual.setName(newName);
        studentDaoJdbcImpl.updateStudent(actual);
        Optional<Student> actual2 = studentDaoJdbcImpl.getStudent(actual.getId());
        assertThat(actual2).isNotEmpty().hasValueSatisfying(studentObj -> assertThat(studentObj.getName()).isEqualTo(newName));

    }

    @Test
    void deleteStudent() {

        String email = FAKER.internet().safeEmailAddress();
        Student student = new Student(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(21, 67)
        );

        studentDaoJdbcImpl.saveStudent(student);

        Student actual = studentDaoJdbcImpl.getAllStudents()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .findFirst()
                .orElseThrow();

        studentDaoJdbcImpl.deleteStudent(actual.getId());
        Optional<Student> actual2 = studentDaoJdbcImpl.getStudent(actual.getId());
        assertThat(actual2).isEmpty();

    }


    @Test
    void existsStudentWithEmail() {

        String email = FAKER.internet().safeEmailAddress();
        Student student = new Student(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(21, 67)
        );

        studentDaoJdbcImpl.saveStudent(student);

        Student actual = studentDaoJdbcImpl.getAllStudents()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .findFirst()
                .orElseThrow();

        assertThat(actual.getEmail()).isEqualTo(email);

    }

    @Test
    void existsStudentWithId() {

        String email = FAKER.internet().safeEmailAddress();
        Student student = new Student(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(21, 67)
        );

        studentDaoJdbcImpl.saveStudent(student);

        Long id = studentDaoJdbcImpl.getAllStudents()
                .stream()
                .filter(s -> s.getEmail().equals(email))
                .map(s -> s.getId())
                .findFirst()
                .orElseThrow();

        Optional<Student> actual = studentDaoJdbcImpl.getStudent(id);
        assertThat(actual).isNotEmpty();

    }
}