package com.demo.app.dao;

import com.demo.app.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Boolean existsStudentByEmail(String email);

}
