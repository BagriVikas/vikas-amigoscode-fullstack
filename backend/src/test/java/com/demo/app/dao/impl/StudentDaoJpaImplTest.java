package com.demo.app.dao.impl;

import com.demo.app.dao.StudentRepository;
import com.demo.app.entity.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class StudentDaoJpaImplTest {

    private StudentDaoJpaImpl underTest;

    @Mock
    private StudentRepository studentRepository;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StudentDaoJpaImpl(studentRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void saveStudent() {

        Student student = new Student();
        student.setName("name");
        student.setEmail("email");
        student.setAge(25);
        underTest.saveStudent(student);
        verify(studentRepository).save(student);

    }

    @Test
    void getStudent() {

        Long id = 1L;
        underTest.getStudent(id);
        verify(studentRepository).findById(id);

    }

    @Test
    void updateStudent() {

        Student student = new Student();
        student.setId(1L);
        student.setName("name");
        student.setEmail("email");
        student.setAge(25);
        underTest.updateStudent(student);
        verify(studentRepository).save(student);

    }

    @Test
    void deleteStudent() {

        Long id = 1L;
        underTest.deleteStudent(id);
        verify(studentRepository).deleteById(id);

    }

    @Test
    void getAllStudents() {

        // call getAllStudents() method of 'underTest'
        underTest.getAllStudents();

        // check whether findAll method is called or not
        verify(studentRepository).findAll();

    }

    @Test
    void existsStudentWithEmail() {

        String email = "email@gmail.com";
        underTest.existsStudentWithEmail(email);
        verify(studentRepository).existsStudentByEmail(email);

    }

    @Test
    void existsStudentWithId() {

        Long id = 1L;
        underTest.existsStudentWithId(id);
        verify(studentRepository).existsById(id);

    }
}