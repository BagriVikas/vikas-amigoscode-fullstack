package com.demo.app.service;

import com.demo.app.dao.StudentDAO;
import com.demo.app.dto.StudentDataUpdate;
import com.demo.app.dto.StudentRegistrationData;
import com.demo.app.entity.Student;
import com.demo.app.exception.NoChangesFoundException;
import com.demo.app.exception.ResourceAlreadyExistsException;
import com.demo.app.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class StudentService {

    private final StudentDAO studentDAO;

    public Long saveStudent(StudentRegistrationData studentRegistrationData) {

        checkForEmail(studentRegistrationData.email());
        Student student = new Student(studentRegistrationData.name(), studentRegistrationData.email(), studentRegistrationData.age());
        studentDAO.saveStudent(student);
        return 1L;

    }

    public Student getStudent(Long id) {
        return studentDAO.getStudent(id).orElseThrow(() -> new ResourceNotFoundException("Student does not exist"));
    }

    public Long updateStudent(Long id, StudentDataUpdate studentDataUpdate) {

        Student student = studentDAO.getStudent(id).orElseThrow(() -> new ResourceNotFoundException("Student does not exist"));
        boolean hasChanges = false;

        if (Objects.nonNull(studentDataUpdate.name()) && !student.getName().equals(studentDataUpdate.name())) {
            student.setName(studentDataUpdate.name());
            hasChanges = true;
        }

        if (Objects.nonNull(studentDataUpdate.email()) && !student.getEmail().equals(studentDataUpdate.email())) {
            boolean existsStudentWithEmail = studentDAO.existsStudentWithEmail(studentDataUpdate.email());
            if (existsStudentWithEmail) {
                throw new ResourceAlreadyExistsException("Email already taken");
            }
            student.setEmail(studentDataUpdate.email());
            hasChanges = true;
        }

        if (Objects.nonNull(studentDataUpdate.age()) && !student.getAge().equals(studentDataUpdate.age())) {
            student.setAge(studentDataUpdate.age());
            hasChanges = true;
        }

        if (!hasChanges) {
            throw new NoChangesFoundException("No changes found");
        }

        studentDAO.updateStudent(student);
        return 1L;

    }

    public String deleteStudent(Long id) {

        boolean existsStudentWithId = studentDAO.existsStudentWithId(id);
        if (!existsStudentWithId) {
            throw new ResourceNotFoundException("Student does not exist");
        }
        studentDAO.deleteStudent(id);
        return "Student deleted successfully";

    }

    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    private void checkForEmail(String email) {

        boolean existsStudentWithEmail = studentDAO.existsStudentWithEmail(email);
        if (existsStudentWithEmail) {
            throw new ResourceAlreadyExistsException("Email already taken");
        }

    }

}
