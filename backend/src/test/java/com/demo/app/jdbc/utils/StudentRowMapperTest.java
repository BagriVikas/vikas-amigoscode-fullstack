package com.demo.app.jdbc.utils;

import com.demo.app.entity.Student;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentRowMapperTest {

    @Test
    void mapRow() throws SQLException {

        StudentRowMapper studentRowMapper = new StudentRowMapper();
        ResultSet resultSet = mock(ResultSet.class);

        long id = 1L;
        String name = "vikas";
        String email = "vikas@gmail.com";
        int age = 25;

        when(resultSet.getLong("id")).thenReturn(id);
        when(resultSet.getString("name")).thenReturn(name);
        when(resultSet.getString("email")).thenReturn(email);
        when(resultSet.getInt("age")).thenReturn(age);

        Student actual = studentRowMapper.mapRow(resultSet, 1);
        Student expected = new Student(id, name, email, age);
        assertThat(actual).isEqualTo(expected);

    }
}