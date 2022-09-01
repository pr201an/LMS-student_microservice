package com.example.studentservice.DTO;

import com.example.studentservice.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class StudentDto {
    private String studentId;
    private String studentName;
    private List<Book> studentIssuedBooks;
    private int maxIssuing;

}
