package ru.hogwarts.school.controllers;

import net.minidev.json.JSONObject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.models.Faculty;
import ru.hogwarts.school.models.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.services.FacultyService;
import ru.hogwarts.school.services.StudentService;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {StudentController.class, StudentService.class, FacultyService.class})
public class TestStudentControllerMwc {

    final private static long TEST_ID = 10000000000L, TEST_ID_FAIL = 99999999999L;
    private static Student student1, student2, studentFail;
    private static Faculty faculty1;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private StudentRepository studentRepository;
    @MockitoBean
    private FacultyRepository facultyRepository;
    @MockitoSpyBean
    private StudentService studentService;
    @MockitoSpyBean
    private FacultyService facultyService;
    @InjectMocks
    private StudentController studentController;

    @BeforeAll
    static void init() {
        faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("Test Faculty 1");
        faculty1.setColor("TestColor");

        student1 = new Student();
        student1.setId(TEST_ID);
        student1.setName("Test Student 1");
        student1.setAge(118);

        student2 = new Student();
        student2.setId(TEST_ID + 1);
        student2.setName("Test Student 2");
        student2.setAge(118);

        studentFail = new Student();
        studentFail.setId(TEST_ID_FAIL);
        studentFail.setName("Test Student Fail");
        studentFail.setAge(118);
    }

    @BeforeEach
    void initBeforeEach() {
        Mockito.when(studentRepository.existsById(student1.getId())).thenReturn(true);
        Mockito.when(studentRepository.findById(student1.getId())).thenReturn(java.util.Optional.of(student1));

        Mockito.when(studentRepository.existsById(student2.getId())).thenReturn(true);
        Mockito.when(studentRepository.findById(student2.getId())).thenReturn(java.util.Optional.of(student2));

        Mockito.when(studentRepository.existsById(studentFail.getId())).thenReturn(false);
    }

    @Test
    void testPostStudent() throws Exception {
        JSONObject facultyJson = new JSONObject();
        facultyJson.put("id", 1L);

        JSONObject studentJson = new JSONObject();
        studentJson.put("name", student1.getName());
        studentJson.put("age", 118);
        studentJson.put("faculty", facultyJson);

        Mockito.when(facultyRepository.existsById(faculty1.getId())).thenReturn(true);
        Mockito.when(facultyRepository.findById(faculty1.getId())).thenReturn(java.util.Optional.of(faculty1));

        Mockito.when(studentRepository.save(any(Student.class))).thenReturn(student1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student") //send
                        .content(studentJson.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.id").value(student1.getId()))
                .andExpect(jsonPath("$.name").value(student1.getName()));
    }

    @Test
    void testGetStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + student1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student1.getId()))
                .andExpect(jsonPath("$.name").value(student1.getName()));
    }

    @Test
    void testGetStudentFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + studentFail.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPutStudent() throws Exception {
        JSONObject facultyJson = new JSONObject();
        facultyJson.put("id", 1L);

        JSONObject studentJson = new JSONObject();
        studentJson.put("name", student2.getName());
        studentJson.put("age", student2.getAge());
        studentJson.put("faculty", facultyJson);

        Mockito.when(facultyRepository.existsById(faculty1.getId())).thenReturn(true);
        Mockito.when(facultyRepository.findById(faculty1.getId())).thenReturn(java.util.Optional.of(faculty1));

        Mockito.when(studentRepository.save(any(Student.class))).thenReturn(student2);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + student1.getId())
                        .content(studentJson.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student2.getId()))
                .andExpect(jsonPath("$.name").value(student2.getName()));
    }

    @Test
    void testPutStudentFail() throws Exception {
        JSONObject studentJson = new JSONObject();
        studentJson.put("name", studentFail.getName());
        studentJson.put("age", studentFail.getAge());

        Mockito.when(studentRepository.save(any(Student.class))).thenReturn(studentFail);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + studentFail.getId())
                        .content(studentJson.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteStudent() throws Exception {
        Mockito.doNothing().when(studentRepository).delete(any(Student.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + student1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student1.getId()))
                .andExpect(jsonPath("$.name").value(student1.getName()));

        Mockito.verify(studentRepository).delete(student1);
    }

    @Test
    void testDeleteStudentFail() throws Exception {
        Mockito.doNothing().when(studentRepository).delete(any(Student.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + studentFail.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        Mockito.verify(studentRepository, Mockito.never()).delete(studentFail);
    }

    @Test
    void testGetStudentsCount() throws Exception {
        Mockito.when(studentRepository.getCountOfStudents()).thenReturn(2);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/get/count")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(2));
    }

    @Test
    void testGetAvgAgeOfStudents() throws Exception {
        Mockito.when(studentRepository.getAvgAgeOfStudents()).thenReturn(118);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/get/avg-age")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(118));
    }

    @Test
    void testGetLastFiveStudents() throws Exception {
        Mockito.when(studentRepository.getLastFiveStudents()).thenReturn(java.util.List.of(student2, student1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/get/last-five")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }
}
