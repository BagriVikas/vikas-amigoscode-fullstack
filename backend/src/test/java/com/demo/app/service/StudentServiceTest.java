package com.demo.app.service;

import com.demo.app.dao.StudentDAO;
import com.demo.app.dto.StudentDataUpdate;
import com.demo.app.dto.StudentRegistrationData;
import com.demo.app.entity.Student;
import com.demo.app.exception.NoChangesFoundException;
import com.demo.app.exception.ResourceAlreadyExistsException;
import com.demo.app.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDAO studentDAO;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentDAO);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void saveStudent() {

        String email = "test@gmail.com";
        when(studentDAO.existsStudentWithEmail(email)).thenReturn(false);

        StudentRegistrationData studentRegistrationData = new StudentRegistrationData("vikas", email, 25);
        underTest.saveStudent(studentRegistrationData);

        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentDAO).saveStudent(studentArgumentCaptor.capture());

        Student actual = studentArgumentCaptor.getValue();
        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo(studentRegistrationData.name());
        assertThat(actual.getEmail()).isEqualTo(studentRegistrationData.email());
        assertThat(actual.getAge()).isEqualTo(studentRegistrationData.age());

    }

    @Test
    void willThrowWhenEmailAlreadyFoundWhileAddingAStudent() {

        String email = "test@gmail.com";
        when(studentDAO.existsStudentWithEmail(email)).thenReturn(true);

        StudentRegistrationData studentRegistrationData = new StudentRegistrationData("vikas", email, 25);
        assertThatThrownBy(() -> underTest.saveStudent(studentRegistrationData)).
                isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessage("Email already taken");

        verify(studentDAO, never()).saveStudent(any());

    }

    @Test
    void canGetStudent() {

        Long id = 1L;
        Student student = new Student(id, "vikas", "vikas@gmail.com", 25);
        when(studentDAO.getStudent(id)).thenReturn(Optional.of(student));

        Student actual = underTest.getStudent(id);
        assertThat(actual).isEqualTo(student);

    }

    @Test
    void willThrowWhenStudentNotFound() {

        Long id = 1L;
        when(studentDAO.getStudent(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getStudent(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Student does not exist");

    }

    @Test
    void updateStudentWithAllUpdatedFields() {

        Long id = 1L;
        Student student = new Student(id, "vikas", "vikas@gmail.com", 25);
        when(studentDAO.getStudent(id)).thenReturn(Optional.of(student));

        String updateName = "newVikas";
        String updateEmail = "new.vikas@gmail.com";
        int updateAge = 27;
        StudentDataUpdate studentDataUpdate = new StudentDataUpdate(updateName, updateEmail, updateAge);

        when(studentDAO.existsStudentWithEmail(updateEmail)).thenReturn(false);

        underTest.updateStudent(id, studentDataUpdate);

        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentDAO).updateStudent(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent.getId()).isEqualTo(id);
        assertThat(capturedStudent.getName()).isEqualTo(updateName);
        assertThat(capturedStudent.getEmail()).isEqualTo(updateEmail);
        assertThat(capturedStudent.getAge()).isEqualTo(updateAge);

    }

    @Test
    void willThrowWhenNoChangeFoundWhileUpdatingStudent() {

        Long id = 1L;
        String name = "vikas";
        String email = "vikas@gmail.com";
        int age = 25;
        Student student = new Student(id, name, email, age);
        when(studentDAO.getStudent(id)).thenReturn(Optional.of(student));

        StudentDataUpdate studentDataUpdate = new StudentDataUpdate(name, email, age);

        assertThatThrownBy(() -> underTest.updateStudent(id, studentDataUpdate))
                .isInstanceOf(NoChangesFoundException.class)
                .hasMessage("No changes found");

        verify(studentDAO, never()).updateStudent(any());

    }

    @Test
    void willThrowWhenStudentNotFoundWithIdSpecifiedWhileUpdatingStudent() {

        Long id = 1L;
        when(studentDAO.getStudent(id)).thenReturn(Optional.empty());

        String name = "vikas";
        String email = "vikas@gmail.com";
        int age = 25;
        StudentDataUpdate studentDataUpdate = new StudentDataUpdate(name, email, age);

        assertThatThrownBy(() -> underTest.updateStudent(id, studentDataUpdate))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Student does not exist");

        verify(studentDAO, never()).updateStudent(any());

    }

    @Test
    void willThrowWhenEmailAlreadyTakenWhileUpdatingStudent() {

        Long id = 1L;
        Student student = new Student(id, "vikas", "vikas@gmail.com", 25);
        when(studentDAO.getStudent(id)).thenReturn(Optional.of(student));

        String updateName = "newVikas";
        String updateEmail = "new.vikas@gmail.com";
        int updateAge = 27;
        StudentDataUpdate studentDataUpdate = new StudentDataUpdate(updateName, updateEmail, updateAge);

        when(studentDAO.existsStudentWithEmail(updateEmail)).thenReturn(true);

        assertThatThrownBy(() -> underTest.updateStudent(id, studentDataUpdate))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessage("Email already taken");

        verify(studentDAO, never()).updateStudent(any());

    }

    @Test
    void deleteStudent() {

        Long id = 1L;
        when(studentDAO.existsStudentWithId(id)).thenReturn(true);

        underTest.deleteStudent(id);
        verify(studentDAO).deleteStudent(id);

    }

    @Test
    void deleteStudentWillThrowWhenIdNotFound() {

        Long id = 1L;
        when(studentDAO.existsStudentWithId(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteStudent(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Student does not exist");

        verify(studentDAO, never()).deleteStudent(any());

    }

    @Test
    void getAllStudents() {

        underTest.getAllStudents();
        verify(studentDAO).getAllStudents();

    }
}