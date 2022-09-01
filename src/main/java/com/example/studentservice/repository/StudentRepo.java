package com.example.studentservice.repository;

import com.example.studentservice.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepo extends MongoRepository<Student, String> {
    @Query("{studentId:'?0'}")
    Optional<Student> findByStudentId(String studentId);
}
