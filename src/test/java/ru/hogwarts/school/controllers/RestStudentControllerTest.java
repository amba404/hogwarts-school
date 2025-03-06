package ru.hogwarts.school.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.models.Faculty;
import ru.hogwarts.school.models.Student;
import ru.hogwarts.school.services.StudentService;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestStudentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentController studentController;

    final private static long TEST_STUDENT_ID = 1000000L, TEST_STUDENT_ID_FAIL = 99999999L;

    private static Student studentTest, studentTestFail;

    @BeforeAll
    public void setup() throws Exception {
        studentTest = new Student();
        studentTest.setId(TEST_STUDENT_ID);
        studentTest.setName("TestStudent");
        studentTest.setAge(118);
        studentTest.setFaculty(new Faculty(1L));

        Assertions.assertThat(studentService.addStudent(studentTest)).isNotNull();

        studentTestFail = new Student();
        studentTestFail.setId(TEST_STUDENT_ID_FAIL);
        studentTestFail.setName("FailStudent");
        studentTestFail.setAge(118);
        studentTest.setFaculty(new Faculty(1L));

    }

    @AfterAll
    public void teardown() throws Exception {
        studentService.findStudentsByAge(studentTest.getAge())
                .forEach(student -> {
                    studentService.deleteStudent(student.getId());
                });
    }

    @Test
    public void testContextLoads() throws Exception {
        Assertions.assertThat(port).isGreaterThan(0);
        Assertions.assertThat(restTemplate).isNotNull();
        Assertions.assertThat(studentService).isNotNull();
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testStudentPost() throws Exception {
        Student studentPost = restTemplate.postForObject("http://localhost:" + port + "/student", studentTest, Student.class);

        Assertions.assertThat(studentPost).isNotNull();
        Assertions.assertThat(studentPost.getId()).isNotEqualByComparingTo(studentTest.getId());
        Assertions.assertThat(studentPost.getName()).isEqualTo(studentTest.getName());
    }

    @Test
    public void testStudentGetById() throws Exception {
        List<Student> allStudents = studentService.getAllStudents();

        Assertions.assertThat(allStudents).isNotEmpty();

        Student studentFound = restTemplate.getForObject("http://localhost:" + port + "/student/" + allStudents.get(0).getId(), Student.class);

        Assertions.assertThat(studentFound).isNotNull();
        Assertions.assertThat(studentFound.getId()).isEqualTo(allStudents.get(0).getId());
    }

    @Test
    public void testStudentGetByIdFails() throws Exception {
        Assertions.assertThatThrownBy(() -> restTemplate.getForObject("http://localhost:"
                                + port + "/student/" + TEST_STUDENT_ID_FAIL,
                        Student.class))
                .isInstanceOf(Exception.class);
    }

    @Test
    public void testStudentPut() throws Exception {

        List<Student> studentsByAge = studentService.findStudentsByAge(studentTest.getAge());

        Assertions.assertThat(studentsByAge).isNotEmpty();

        Student student = studentsByAge.get(0);
        student.setName("PutStudent");

        restTemplate.put("http://localhost:" + port + "/student/" + student.getId(), student);
        Student studentPut = restTemplate.getForObject("http://localhost:" + port + "/student/" + student.getId(), Student.class);

        Assertions.assertThat(studentPut).isNotNull();
        Assertions.assertThat(studentPut.getName()).isEqualTo(student.getName());
    }

    @Test
    public void testStudentPutFails() throws Exception {
        restTemplate.put("http://localhost:"
                        + port + "/student/" + TEST_STUDENT_ID_FAIL,
                studentTestFail);
        Assertions.assertThatThrownBy(() -> restTemplate.getForEntity("http://localhost:"
                        + port + "/student/" + TEST_STUDENT_ID_FAIL,
                Student.class)).isInstanceOf(Exception.class);


    }

    @Test
    public void testStudentDelete() throws Exception {
        studentService.addStudent(studentTest);

        List<Student> studentsByAge = studentService.findStudentsByAge(studentTest.getAge());

        Assertions.assertThat(studentsByAge).isNotEmpty();

        Student student = studentsByAge.get(0);

        restTemplate.delete("http://localhost:" + port + "/student/" + student.getId());

        Assertions.assertThatThrownBy(() -> restTemplate.getForEntity("http://localhost:"
                                + port + "/student/" + student.getId(),
                        Student.class))
                .isInstanceOf(Exception.class);
    }

    @Test
    public void testFindStudentByAge() throws Exception {
        ResponseEntity<List<Student>> studentsByAge = restTemplate.exchange("http://localhost:" + port + "/student/find/age?age=" + studentTest.getAge(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>(){});

        Assertions.assertThat(studentsByAge.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(studentsByAge.getBody()).isNotEmpty();
    }

}
