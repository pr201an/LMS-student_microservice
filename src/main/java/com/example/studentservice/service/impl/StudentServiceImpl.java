package com.example.studentservice.service.impl;

import com.example.studentservice.DTO.StudentDto;
import com.example.studentservice.ExceptionHandling.CustomException;
import com.example.studentservice.model.Book;
import com.example.studentservice.model.Student;
import com.example.studentservice.repository.StudentRepo;
import com.example.studentservice.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    RestTemplate restTemplate;
    private final StudentRepo studentRepo;

    public StudentServiceImpl(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    @Override
    public List<StudentDto> getAllStudentsDto() {
        return studentRepo.findAll().stream().map(student -> {
            return modelMapper.map(student, StudentDto.class);
        }).collect(Collectors.toList());
    }

    @Override
    @Cacheable(cacheNames = "students", key = "#studentId")
    public Student getStudentByStudentId(String studentId) {
        if(!studentRepo.findByStudentId(studentId).isPresent()){
            throw new NoSuchElementException("The Requested Student with id:" + studentId + "is not available");
        }
        System.out.println("Not returning from Cache");
        Optional<Student> optional = studentRepo.findByStudentId(studentId);
        return optional.get();
    }

    @Override
    @CachePut(cacheNames = "students", key = "#student.studentId")
    public Student saveStudent(Student student) {
        return studentRepo.save(student);
    }

    @Override
    public Student addStudent(String studentId, String studentName) throws CustomException {
        Optional<Student> optional = studentRepo.findByStudentId(studentId);
        if(optional.isPresent())
            throw new CustomException("Student with the Given Student Id already present");
        Student student = new Student();
        student.setStudentId(studentId);
        student.setStudentName(studentName);
        student.setStudentIssuedBooks(new ArrayList<>());
        student.setMaxIssuing(2);
        return studentRepo.save(student);
    }

    @Override
    public Book getBook(String bookName) throws CustomException {
        Book book = restTemplate.getForObject("http://localhost:8080/book/"+bookName, Book.class);
        if(book == null)
            throw new CustomException("The Book Requested is Not Present in the Library. Check Back Later!!");
        return book;
    }

    @Override
    @CachePut(cacheNames = "students", key = "#studentId")
    public Student getUpdatedStudentAfterIssue(String studentId, String bookName) {
        Student student = studentRepo.findByStudentId(studentId).get();
        Book book = restTemplate.getForObject("http://localhost:8080/book/"+bookName, Book.class);
        assert book != null;
        if(student.getStudentIssuedBooks().size() < 2){
            if(Objects.equals(book.getBookStatus(), "Present")){
                book.setBookStatus("Absent");
                restTemplate.put("http://localhost:8080/book/" + bookName, book);
                student.getStudentIssuedBooks().add(book);
            }
        }
        return studentRepo.save(student);
    }

    @Override
    public String getMessageAfterIssue(String studentId, String bookName) {
        Student student = studentRepo.findByStudentId(studentId).get();
        Book book = restTemplate.getForObject("http://localhost:8080/book/"+bookName, Book.class);
        assert book != null;
        if(student.getStudentIssuedBooks().size() >= 2){
            return "Maximum Quantity of Books Borrowed";
        }
        else{
            if(Objects.equals(book.getBookStatus(), "Absent")){
                return "The Book requested is currently unavailable";
            }
            else {
                return "The Book has been Borrowed";
            }
        }
    }

    @Override
    @CachePut(cacheNames = "students", key = "#studentId")
    public Student getUpdatedStudentAfterReturn(String studentId, String bookName) {
        Student student = studentRepo.findByStudentId(studentId).get();
        Book book = restTemplate.getForObject("http://localhost:8080/book/"+bookName, Book.class);
        assert book != null;
        if(student.getStudentIssuedBooks().size() > 0){
            int i;
            Book tempBook = new Book();
            for(i=0; i<student.getStudentIssuedBooks().size(); i++){
                tempBook = student.getStudentIssuedBooks().get(i);
                if(i == student.getStudentIssuedBooks().size()-1 && !Objects.equals(tempBook.getBookName(), bookName)){
                    return student;
                }
                if(Objects.equals(tempBook.getBookName(), bookName))
                    break;
            }
            student.getStudentIssuedBooks().remove(tempBook);

            book.setBookStatus("Present");
            restTemplate.put("http://localhost:8080/book/" + bookName, book);
        }
        return studentRepo.save(student);
    }

    @Override
    public String getMessageAfterReturn(String studentId, String bookName) {
        Student student = studentRepo.findByStudentId(studentId).get();
        Book book = restTemplate.getForObject("http://localhost:8080/book/"+bookName, Book.class);
        assert book != null;
        if(student.getStudentIssuedBooks().size() == 0){
            return "No Book Borrowed to Return!";
        }
        else{
            int i;
            Book tempBook = new Book();
            for(i=0; i<student.getStudentIssuedBooks().size(); i++){
                tempBook = student.getStudentIssuedBooks().get(i);
                if(i == student.getStudentIssuedBooks().size()-1 && !Objects.equals(tempBook.getBookName(), bookName)){
                    return "Book cannot be returned as it has not been borrowed";
                }
            }
            return "Book Successfully Returned!";
        }
    }

    @Override
    @CacheEvict(cacheNames = "students", key = "#studentId")
    public void deleteStudentByStudentID(String studentId) {
        Student student = studentRepo.findByStudentId(studentId).get();
        studentRepo.deleteById(student.getId());
    }

    @Override
    @CacheEvict(cacheNames = "students", allEntries = true)
    public void deleteAllStudents() {
        studentRepo.deleteAll();
    }
}
