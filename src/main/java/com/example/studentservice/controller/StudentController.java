package com.example.studentservice.controller;

import com.example.studentservice.DTO.StudentDto;
import com.example.studentservice.ExceptionHandling.CustomException;
import com.example.studentservice.model.Book;
import com.example.studentservice.model.Student;
import com.example.studentservice.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        return new ResponseEntity<>(studentService.getAllStudents(), HttpStatus.OK);
    }

    @GetMapping("/students/cache")
    public ResponseEntity<List<StudentDto>> getAllStudentsDto() {
        return new ResponseEntity<>(studentService.getAllStudentsDto(), HttpStatus.OK);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable("studentId") String studentId) {
        return new ResponseEntity<>(studentService.getStudentByStudentId(studentId), HttpStatus.OK);
    }

    @PostMapping("/student/add/{studentId}/{studentName}")
    public ResponseEntity<Student> addStudent
            (@PathVariable String studentId, @PathVariable String studentName) throws CustomException {
        return new ResponseEntity<>(studentService.addStudent(studentId, studentName), HttpStatus.CREATED);
    }

    @GetMapping("/getBook/{bookName}")
    public ResponseEntity<Book> getBookFromLmsService (@PathVariable("bookName") String bookName) throws CustomException {
        return new ResponseEntity<>(studentService.getBook(bookName), HttpStatus.OK);
    }

    @PatchMapping("/student/issue/{studentId}/{bookName}")
    public ResponseEntity<?> issueBook(@PathVariable("studentId") String studentId,
                                       @PathVariable("bookName") String bookName) {
        String message = studentService.getMessageAfterIssue(studentId, bookName);
        studentService.getUpdatedStudentAfterIssue(studentId, bookName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PatchMapping("/student/return/{studentId}/{bookName}")
    public ResponseEntity<?> returnBook(@PathVariable("studentId") String studentId,
                                        @PathVariable("bookName") String bookName) {
        String message = studentService.getMessageAfterReturn(studentId, bookName);
        studentService.getUpdatedStudentAfterReturn(studentId, bookName);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("student/delete/{studentId}")
    public void deleteStudentById (@PathVariable("studentId") String studentId) {
        studentService.deleteStudentByStudentID(studentId);
    }

    @DeleteMapping("student/empty")
    public void deleteAllStudents() {
        studentService.deleteAllStudents();
    }

}



//    @PatchMapping("/student/issue/{studentId}/{bookName}")
//    public ResponseEntity<?> issueBook(@PathVariable("studentId") String studentId,
//                                    @PathVariable("bookName") String bookName) {
//        Student student = studentService.getStudentByStudentId(studentId);
//        if(student.getStudentIssuedBooks().size() >= 2){
//            return new ResponseEntity<>("Maximum Quantity of Books Borrowed", HttpStatus.OK);
//        }
//        else{
//            Book book = restTemplate.getForObject("http://localhost:8080/book/"+bookName, Book.class);
//            assert book != null;
//            if(Objects.equals(book.getBookStatus(), "Absent")){
//                return new ResponseEntity<>("The Book requested is currently unavailable", HttpStatus.OK);
//            }
//            else {
//                book.setBookStatus("Absent");
//                restTemplate.put("http://localhost:8080/book/" + bookName, book);
//                student.getStudentIssuedBooks().add(book);
//                studentService.saveStudent(student);
//                return new ResponseEntity<>("The Book has been Borrowed", HttpStatus.OK);
//            }
//        }
//    }

//    @PatchMapping("/student/return/{studentId}/{bookName}")
//    public ResponseEntity<?> returnBook(@PathVariable("studentId") String studentId,
//                                       @PathVariable("bookName") String bookName) {
//        Student student = studentService.getStudentByStudentId(studentId);
//        if(student.getStudentIssuedBooks().size() == 0){
//            return new ResponseEntity<>("No Book Borrowed to Return!", HttpStatus.OK);
//        }
//        else{
//            Book book = restTemplate.getForObject("http://localhost:8080/book/"+bookName, Book.class);
//            assert book != null;
//            //student.getStudentIssuedBooks().remove(book);
//            int i;
//            Book tempBook = new Book();
//            for(i=0; i<student.getStudentIssuedBooks().size(); i++){
//                tempBook = student.getStudentIssuedBooks().get(i);
//                if(i == student.getStudentIssuedBooks().size()-1 && !Objects.equals(tempBook.getBookName(), bookName)){
//                    return new ResponseEntity<>("Book cannot be returned as it has not been borrowed", HttpStatus.OK);
//                }
//                if(Objects.equals(tempBook.getBookName(), bookName))
//                    break;
//            }
//            student.getStudentIssuedBooks().remove(tempBook);
//            studentService.saveStudent(student);
//
//            book.setBookStatus("Present");
//            restTemplate.put("http://localhost:8080/book/" + bookName, book);
//
//            return new ResponseEntity<>("Book Successfully Returned!", HttpStatus.OK);
//        }
//    }
