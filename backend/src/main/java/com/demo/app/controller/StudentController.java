package com.demo.app.controller;

import com.demo.app.dto.StudentDataUpdate;
import com.demo.app.dto.StudentRegistrationData;
import com.demo.app.entity.Student;
import com.demo.app.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public Long saveStudent(@RequestBody StudentRegistrationData studentRegistrationData) {
        return studentService.saveStudent(studentRegistrationData);
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable(name = "id") Long id) {
        return studentService.getStudent(id);
    }

    @PutMapping("/{id}")
    public Long updateStudent(@PathVariable(name = "id") Long id,
                              @RequestBody StudentDataUpdate studentDataUpdate) {
        return studentService.updateStudent(id, studentDataUpdate);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable(name = "id") Long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

}
