package com.demo.app.dao;

import com.demo.app.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDAO {

    void saveStudent(Student student);

    Optional<Student> getStudent(Long id);

    void updateStudent(Student student);

    void deleteStudent(Long id);

    List<Student> getAllStudents();

    boolean existsStudentWithEmail(String email);

    boolean existsStudentWithId(Long id);

}
