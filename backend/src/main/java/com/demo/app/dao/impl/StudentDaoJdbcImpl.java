package com.demo.app.dao.impl;

import com.demo.app.dao.StudentDAO;
import com.demo.app.entity.Student;
import com.demo.app.jdbc.utils.StudentRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Primary
public class StudentDaoJdbcImpl implements StudentDAO {

    private final JdbcTemplate jdbcTemplate;

    private final StudentRowMapper studentRowMapper;

    @Override
    public void saveStudent(Student student) {

        String sql = """
                INSERT INTO student( name, email, age )
                VALUES( ?, ?, ? )
                """;
        jdbcTemplate.update(sql, student.getName(), student.getEmail(), student.getAge());
    }

    @Override
    public Optional<Student> getStudent(Long id) {

        String sql = """
                SELECT id, name, email, age FROM student
                WHERE id = ?
                """;
        List<Student> students = jdbcTemplate.query(sql, studentRowMapper, id);
        return students.stream()
                .filter(student -> student.getId() == id)
                .findFirst();

    }

    @Override
    public void updateStudent(Student student) {

        String sql = """
                UPDATE student
                SET name = ?, email = ?, age = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, student.getName(), student.getEmail(), student.getAge(), student.getId());

    }

    @Override
    public void deleteStudent(Long id) {
        String sql = """
                DELETE FROM student WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Student> getAllStudents() {
        String sql = """
                SELECT id, name, email, age FROM student
                """;
        return jdbcTemplate.query(sql, studentRowMapper);
    }

    @Override
    public boolean existsStudentWithEmail(String email) {

        String sql = """
                SELECT COUNT( id ) FROM student
                WHERE email = ?
                """;
        long count = jdbcTemplate.queryForObject(sql, Long.class, email);
        return count > 0;

    }

    @Override
    public boolean existsStudentWithId(Long id) {

        String sql = """
                SELECT id, name, email, age FROM student
                WHERE id = ?
                """;
        List<Student> students = jdbcTemplate.query(sql, studentRowMapper, id);
        return !students.isEmpty();

    }

}
