package com.demo.app.dao.impl;

import com.demo.app.dao.StudentDAO;
import com.demo.app.dao.StudentRepository;
import com.demo.app.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class StudentDaoJpaImpl implements StudentDAO {

    private final StudentRepository studentRepository;
    @Override
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    public Optional<Student> getStudent(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public void updateStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> getAllStudents() {
       return studentRepository.findAll();
    }

    @Override
    public boolean existsStudentWithEmail(String email) {
        return studentRepository.existsStudentByEmail(email);
    }

    @Override
    public boolean existsStudentWithId(Long id) {
        return studentRepository.existsById(id);
    }
}
