package ru.hogwarts.school.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.models.Faculty;
import ru.hogwarts.school.services.FacultyService;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FacultyControllerTestRest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private FacultyController facultyController;

    final private static long TEST_FACULTY_ID = 1000000L, TEST_FACULTY_ID_FAIL  = 99999999L;

    private static Faculty facultyTest;

    @BeforeAll
    public void setup() throws Exception{
        facultyTest = new Faculty();
        facultyTest.setId(TEST_FACULTY_ID);
        facultyTest.setName("TestFaculty");
        facultyTest.setColor("TestColor");

//        Assertions.assertThat(facultyService.addFaculty(facultyTest)).isNotNull();
    }

    @AfterAll
    public void teardown() throws Exception{
        facultyService.findFacultiesByColor(facultyTest.getColor())
                .forEach(faculty -> {
                    facultyService.deleteFaculty(faculty.getId());
                });
    }

    @Test
    public void testContextLoads() throws Exception{
        Assertions.assertThat(port).isGreaterThan(0);
        Assertions.assertThat(restTemplate).isNotNull();
        Assertions.assertThat(facultyService).isNotNull();
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    public void testFacultyPost() throws Exception{
        Faculty facultyPost = restTemplate.postForObject("http://localhost:" + port + "/faculty", facultyTest, Faculty.class);

        Assertions.assertThat(facultyPost).isNotNull();
        Assertions.assertThat(facultyPost.getId()).isNotEqualByComparingTo(facultyTest.getId());
    }

    @Test
    public void testFacultyGetById() throws Exception{
        List<Faculty> allFaculties = facultyService.getAllFaculties();

        Assertions.assertThat(allFaculties).isNotEmpty();

        Faculty facultyFound = restTemplate.getForObject("http://localhost:" + port + "/faculty/" + allFaculties.get(0).getId(), Faculty.class);

        Assertions.assertThat(facultyFound).isNotNull();
        Assertions.assertThat(facultyFound.getId()).isEqualTo(allFaculties.get(0).getId());
    }

    @Test
    public void testFacultyGetByIdFails() throws Exception{
        Assertions.assertThatThrownBy(() -> restTemplate.getForObject("http://localhost:"
                        + port + "/faculty/" + TEST_FACULTY_ID_FAIL,
                        Faculty.class))
                .isInstanceOf(Exception.class);
    }

    @Test
    public void testFacultyPut() throws Exception{

        List<Faculty> facultiesByColor = facultyService.findFacultiesByColor(facultyTest.getColor());

        Assertions.assertThat(facultiesByColor).isNotEmpty();

        Faculty faculty = facultiesByColor.get(0);
        faculty.setName("PutFaculty");

        Faculty facultyPut = restTemplate.postForObject("http://localhost:" + port + "/faculty/"+ faculty.getId(), faculty, Faculty.class);

        Assertions.assertThat(facultyPut).isNotNull();
        Assertions.assertThat(facultyPut.getName()).isEqualTo(faculty.getName());
    }

    @Test
    public void testFacultyPutFails() throws Exception{
        Assertions.assertThatThrownBy(() -> restTemplate.put("http://localhost:"
                        + port + "/faculty/" + TEST_FACULTY_ID_FAIL,
                        facultyTest,
                        Faculty.class))
                .isInstanceOf(Exception.class);
    }


}
