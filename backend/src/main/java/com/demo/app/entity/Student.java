package com.demo.app.entity;

import com.demo.app.dto.StudentRegistrationData;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(
        name = "student",
        uniqueConstraints = {
                @UniqueConstraint(name = "email_unique", columnNames = {"email"})
        }
)
public class Student {

    @Id
    @SequenceGenerator(
            name = "student_id_seq_generator",
            sequenceName = "student_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_id_seq_generator"
    )
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private Integer age;

    public Student(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Student(StudentRegistrationData studentRegistrationData) {
        this.name = studentRegistrationData.name();
        this.email = studentRegistrationData.email();
        this.age = studentRegistrationData.age();
    }

}
