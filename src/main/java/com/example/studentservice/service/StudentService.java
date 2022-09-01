package com.example.studentservice.service;

import com.example.studentservice.DTO.StudentDto;
import com.example.studentservice.ExceptionHandling.CustomException;
import com.example.studentservice.model.Book;
import com.example.studentservice.model.Student;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    List<StudentDto> getAllStudentsDto();
    Student getStudentByStudentId(String studentId);
    Student saveStudent(Student student);
    Student addStudent(Student student) throws CustomException;
    Book getBook(String bookName) throws CustomException;
    Student getUpdatedStudentAfterIssue(String studentId, String bookName);
    String getMessageAfterIssue(String studentId, String bookName);
    Student getUpdatedStudentAfterReturn(String studentId, String bookName);
    String getMessageAfterReturn(String studentId, String bookName);
    void deleteStudentByStudentID(String studentId);
    void deleteAllStudents();
}
