package com.example.studentservice.controller;

import com.example.studentservice.ExceptionHandling.CustomException;
import com.example.studentservice.model.Student;
import com.example.studentservice.service.impl.StudentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private StudentController studentController;
    @Mock
    private StudentServiceImpl studentService;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws Exception{
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }
    @Test
    public void shouldCreateMockMvc() {
        Assertions.assertNotNull(mockMvc);
    }

    @Test
    public void shouldReturnListOfUsers() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of(
                new Student("1L","30","Aadu", new ArrayList<>(), 2)));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/students").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].studentName").value("Aadu"));
    }

    @Test
    public void shouldCreateUser() throws Exception {
        Student student = new Student("1L","30","Aadu", new ArrayList<>(), 2);
        Mockito.when(studentService.addStudent(Mockito.any(Student.class))).thenReturn(student);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/student/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(student)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        String request = result.getResponse().getContentAsString();
        Student student1 = mapper.readValue(request, Student.class);
        Assertions.assertEquals(student1.getStudentName(), "Aadu");
    }

    @Test
    public void shouldIssueBookToAStudent() throws Exception {
        Student student = new Student("1L","30","Aadu", new ArrayList<>(), 2);
        Mockito.when(studentService.getMessageAfterIssue("30", "Matilda"))
                .thenReturn("The Book has been Borrowed");
//        Mockito.when(studentService.getUpdatedStudentAfterIssue("30", "Matilda"))
//                .thenReturn(student);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/student/issue/30/Matilda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(student)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String request = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(request, "The Book has been Borrowed");
    }
}