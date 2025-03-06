package ru.hogwarts.school.controllers;

import net.minidev.json.JSONObject;
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
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.services.FacultyService;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {FacultyController.class, FacultyService.class})
public class TestFacultyControllerMwc {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyRepository facultyRepository;

    @MockitoSpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    private static Faculty faculty1, faculty2, facultyFail;

    final private static long TEST_ID = 10000000000L, TEST_ID_FAIL = 99999999999L;

    @BeforeAll
    static void init() {
        faculty1 = new Faculty();
        faculty1.setId(TEST_ID);
        faculty1.setName("Test Faculty 1");
        faculty1.setColor("TestColor");

        faculty2 = new Faculty();
        faculty2.setId(TEST_ID + 1);
        faculty2.setName("Test Faculty 2");
        faculty2.setColor("TestColor");

        facultyFail = new Faculty();
        facultyFail.setId(TEST_ID_FAIL);
        facultyFail.setName("Test Faculty Fail");
        facultyFail.setColor("TestColor");
    }

    @BeforeEach
    void initBeforeEach() {
        Mockito.when(facultyRepository.existsById(faculty1.getId())).thenReturn(true);
        Mockito.when(facultyRepository.findById(faculty1.getId())).thenReturn(java.util.Optional.of(faculty1));

        Mockito.when(facultyRepository.existsById(faculty2.getId())).thenReturn(true);
        Mockito.when(facultyRepository.findById(faculty2.getId())).thenReturn(java.util.Optional.of(faculty2));

        Mockito.when(facultyRepository.existsById(facultyFail.getId())).thenReturn(false);
    }

    @Test
    void testPostFaculty() throws Exception {
        JSONObject facultyJson = new JSONObject();
        facultyJson.put("name", faculty1.getName());
        facultyJson.put("color", faculty1.getColor());

        Mockito.when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty") //send
                        .content(facultyJson.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.id").value(faculty1.getId()))
                .andExpect(jsonPath("$.name").value(faculty1.getName()));
    }

    @Test
    void testGetFaculty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + faculty1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty1.getId()))
                .andExpect(jsonPath("$.name").value(faculty1.getName()));
    }

    @Test
    void testGetFacultyFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + facultyFail.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPutFaculty() throws Exception {
        JSONObject facultyJson = new JSONObject();
        facultyJson.put("name", faculty2.getName());
        facultyJson.put("color", faculty2.getColor());

        Mockito.when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty2);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + faculty1.getId())
                .content(facultyJson.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty2.getId()))
                .andExpect(jsonPath("$.name").value(faculty2.getName()));
    }

    @Test
    void testPutFacultyFail() throws Exception {
        JSONObject facultyJson = new JSONObject();
        facultyJson.put("name", facultyFail.getName());
        facultyJson.put("color", facultyFail.getColor());

        Mockito.when(facultyRepository.save(any(Faculty.class))).thenReturn(facultyFail);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + facultyFail.getId())
                .content(facultyJson.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteFaculty() throws Exception {
        Mockito.doNothing().when(facultyRepository).delete(any(Faculty.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + faculty1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty1.getId()))
                .andExpect(jsonPath("$.name").value(faculty1.getName()));

        Mockito.verify(facultyRepository).delete(faculty1);
    }

    @Test
    void testDeleteFacultyFail() throws Exception {
        Mockito.doNothing().when(facultyRepository).delete(any(Faculty.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + facultyFail.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        Mockito.verify(facultyRepository, Mockito.never()).delete(facultyFail);
    }
}
