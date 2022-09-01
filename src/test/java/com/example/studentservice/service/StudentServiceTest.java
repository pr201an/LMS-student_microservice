package com.example.studentservice.service;

import com.example.studentservice.model.Student;
import com.example.studentservice.repository.StudentRepo;
import com.example.studentservice.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepo studentRepo;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    @BeforeEach
    public void setUp() {
        student = Student.builder().id("1L")
                .studentName("Aadu")
                .studentId("30")
                .maxIssuing(2)
                .studentIssuedBooks(new ArrayList<>())
                .build();
    }

    @Test
    public void saveBooks() {
        given(studentRepo.save(student)).willReturn(student);
        // when -  action or the behaviour that we are going test
        Student savedStudent = studentService.saveStudent(student);

        System.out.println(savedStudent);
        // then - verify the output
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getStudentName()).isEqualTo("Aadu");
    }

    @Test
    public void getAllBooks() {
        Student student1 = Student.builder()
                .studentName("Vijay")
                .studentId("31")
                .maxIssuing(2)
                .studentIssuedBooks(new ArrayList<>())
                .build();
        given(studentRepo.findAll()).willReturn(List.of(student, student1));
        //bookService.saveBook(book);
        List<Student> list = studentService.getAllStudents();
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void getBookByName() {
        given(studentRepo.findByStudentId("30")).willReturn(Optional.ofNullable(student));
        Student savedStudent = studentService.getStudentByStudentId(student.getStudentId());
        assertThat(savedStudent).isNotNull();
    }

    @Test
    public void updateBook() {
        given(studentRepo.save(student)).willReturn(student);
        student.setStudentName("Aadoo");

        Student updatedBook = studentService.saveStudent(student);
        assertThat(updatedBook.getStudentName()).isEqualTo("Aadoo");
    }
}