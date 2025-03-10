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
import ru.hogwarts.school.services.FacultyService;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestFacultyControllerTest {
    final private static long TEST_FACULTY_ID = 1000000L, TEST_FACULTY_ID_FAIL = 99999999L;
    private static Faculty facultyTest, facultyTestFail;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private FacultyController facultyController;

    @BeforeAll
    public void setup() throws Exception {
        facultyTest = new Faculty();
        facultyTest.setId(TEST_FACULTY_ID);
        facultyTest.setName("TestFaculty");
        facultyTest.setColor("TestColor");

        Assertions.assertThat(facultyService.addFaculty(facultyTest)).isNotNull();

        facultyTestFail = new Faculty();
        facultyTestFail.setId(TEST_FACULTY_ID_FAIL);
        facultyTestFail.setName("FailFaculty");
        facultyTestFail.setColor("TestColor");

    }

    @AfterAll
    public void teardown() throws Exception {
        facultyService.findFacultiesByColor(facultyTest.getColor())
                .forEach(faculty -> {
                    facultyService.deleteFaculty(faculty.getId());
                });
    }

    @Test
    public void testContextLoads() throws Exception {
        Assertions.assertThat(port).isGreaterThan(0);
        Assertions.assertThat(restTemplate).isNotNull();
        Assertions.assertThat(facultyService).isNotNull();
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    public void testFacultyPost() throws Exception {
        Faculty facultyPost = restTemplate.postForObject("http://localhost:" + port + "/faculty", facultyTest, Faculty.class);

        Assertions.assertThat(facultyPost).isNotNull();
        Assertions.assertThat(facultyPost.getId()).isNotEqualByComparingTo(facultyTest.getId());
        Assertions.assertThat(facultyPost.getName()).isEqualTo(facultyTest.getName());
    }

    @Test
    public void testFacultyGetById() throws Exception {
        List<Faculty> allFaculties = facultyService.getAllFaculties();

        Assertions.assertThat(allFaculties).isNotEmpty();

        Faculty facultyFound = restTemplate.getForObject("http://localhost:" + port + "/faculty/" + allFaculties.get(0).getId(), Faculty.class);

        Assertions.assertThat(facultyFound).isNotNull();
        Assertions.assertThat(facultyFound.getId()).isEqualTo(allFaculties.get(0).getId());
    }

    @Test
    public void testFacultyGetByIdFails() throws Exception {
        Assertions.assertThatThrownBy(() -> restTemplate.getForObject("http://localhost:"
                                + port + "/faculty/" + TEST_FACULTY_ID_FAIL,
                        Faculty.class))
                .isInstanceOf(Exception.class);
    }

    @Test
    public void testFacultyPut() throws Exception {

        List<Faculty> facultiesByColor = facultyService.findFacultiesByColor(facultyTest.getColor());

        Assertions.assertThat(facultiesByColor).isNotEmpty();

        Faculty faculty = facultiesByColor.get(0);
        faculty.setName("PutFaculty");

        restTemplate.put("http://localhost:" + port + "/faculty/" + faculty.getId(), faculty);
        Faculty facultyPut = restTemplate.getForObject("http://localhost:" + port + "/faculty/" + faculty.getId(), Faculty.class);

        Assertions.assertThat(facultyPut).isNotNull();
        Assertions.assertThat(facultyPut.getName()).isEqualTo(faculty.getName());
    }

    @Test
    public void testFacultyPutFails() throws Exception {
        restTemplate.put("http://localhost:"
                        + port + "/faculty/" + TEST_FACULTY_ID_FAIL,
                facultyTestFail);
        Assertions.assertThatThrownBy(() -> restTemplate.getForEntity("http://localhost:"
                        + port + "/faculty/" + TEST_FACULTY_ID_FAIL,
                Faculty.class)).isInstanceOf(Exception.class);


    }

    @Test
    public void testFacultyDelete() throws Exception {
        facultyService.addFaculty(facultyTest);

        List<Faculty> facultiesByColor = facultyService.findFacultiesByColor(facultyTest.getColor());

        Assertions.assertThat(facultiesByColor).isNotEmpty();

        Faculty faculty = facultiesByColor.get(0);

        restTemplate.delete("http://localhost:" + port + "/faculty/" + faculty.getId());

        Assertions.assertThatThrownBy(() -> restTemplate.getForEntity("http://localhost:"
                                + port + "/faculty/" + faculty.getId(),
                        Faculty.class))
                .isInstanceOf(Exception.class);
    }

    @Test
    public void testFindFacultyByColor() throws Exception {
        ResponseEntity<List<Faculty>> facultiesByColor = restTemplate.exchange("http://localhost:" + port + "/faculty/find/color/" + facultyTest.getColor(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                });

        Assertions.assertThat(facultiesByColor.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(facultiesByColor.getBody()).isNotEmpty();
    }

}
