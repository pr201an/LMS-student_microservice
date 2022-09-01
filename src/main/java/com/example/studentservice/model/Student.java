package com.example.studentservice.model;

import jdk.jfr.DataAmount;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Document("student_info")
@Builder
public class Student implements Serializable {
    @Id
    private String id;
    @NotBlank(message = "Student Id cannot be Empty")
    private String studentId;
    @NotBlank(message = "Student Name cannot be Empty")
    private String studentName;
    private List<Book> studentIssuedBooks;
    private int maxIssuing;

    public Student() {
    }

    public Student(String id, String studentId, String studentName, List<Book> studentIssuedBooks, int maxIssuing) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentIssuedBooks = studentIssuedBooks;
        this.maxIssuing = maxIssuing;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public List<Book> getStudentIssuedBooks() {
        return studentIssuedBooks;
    }

    public void setStudentIssuedBooks(List<Book> studentIssuedBooks) {
        this.studentIssuedBooks = studentIssuedBooks;
    }

    public int getMaxIssuing() {
        return maxIssuing;
    }

    public void setMaxIssuing(int maxIssuing) {
        this.maxIssuing = maxIssuing;
    }
}
