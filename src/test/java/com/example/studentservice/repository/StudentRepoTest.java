package com.example.studentservice.repository;

import com.example.studentservice.model.Student;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentRepoTest {
    @Autowired
    private StudentRepo studentRepo;

    @BeforeEach
    void setUp() {
    }

    @Test
    @Order(1)
    public void saveEmployeeTest() {
        List empty = new ArrayList<>();
        Student student = new Student("1L","30", "Aadu", empty, 2);
        assertEquals(student, studentRepo.save(student));
    }

    @Test
    @Order(2)
    public void getEmployeeTest() {
        Student student = studentRepo.findById("1L").get();
        assertThat(student.getId()).isEqualTo("1L");
    }

    @Test
    @Order(3)
    public void getListOfEmployeesTest() {
        List<Student> employees = studentRepo.findAll();
        assertThat(employees.size()).isGreaterThanOrEqualTo(6);
    }

    @Test
    @Order(4)
    public void updateEmployeesTest() {
        Student student = studentRepo.findById("1L").get();
        student.setStudentName("Aadoo");
        Student studentUpdated = studentRepo.save(student);
        assertThat(studentUpdated.getStudentName()).isEqualTo("Aadoo");
    }

    @Test
    @Order(5)
    public void deleteEmployeeTest() {
        Optional<Student> student = studentRepo.findByStudentId("30");
        studentRepo.delete(student.get());
        Optional<Student> optionalStudent = studentRepo.findById("1L");
        assertThat(optionalStudent).isEmpty();
    }
}