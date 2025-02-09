package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {

    @InjectMocks
    private FacultyService facultyService;

    @Test
    public void testAddFaculty() {
        Faculty faculty = new Faculty();
        faculty.setColor("red");
        Faculty addedFaculty = facultyService.addFaculty(faculty);
        assertNotNull(addedFaculty);
        assertEquals(1L, addedFaculty.getId());
    }

    @Test
    public void testGetFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setColor("red");
        facultyService.addFaculty(faculty);
        Faculty retrievedFaculty = facultyService.getFaculty(1L);
        assertNotNull(retrievedFaculty);
        assertEquals(1L, retrievedFaculty.getId());
    }

    @Test
    public void testUpdateFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setColor("red");
        facultyService.addFaculty(faculty);
        faculty.setColor("blue");
        Faculty updatedFaculty = facultyService.updateFaculty(faculty);
        assertNotNull(updatedFaculty);
        assertEquals("blue", updatedFaculty.getColor());
    }

    @Test
    public void testUpdateFacultyNotFound() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setColor("red");
        Faculty updatedFaculty = facultyService.updateFaculty(faculty);
        assertNull(updatedFaculty);
    }

    @Test
    public void testDeleteFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setColor("red");
        facultyService.addFaculty(faculty);
        Faculty deletedFaculty = facultyService.deleteFaculty(1L);
        assertNotNull(deletedFaculty);
        assertEquals(1L, deletedFaculty.getId());
    }

    @Test
    public void testDeleteFacultyNotFound() {
        Faculty deletedFaculty = facultyService.deleteFaculty(1L);
        assertNull(deletedFaculty);
    }

    @Test
    public void testGetAllFaculties() {
        Faculty faculty1 = new Faculty();
        faculty1.setColor("red");
        facultyService.addFaculty(faculty1);
        Faculty faculty2 = new Faculty();
        faculty2.setColor("blue");
        facultyService.addFaculty(faculty2);
        Collection<Faculty> allFaculties = facultyService.getAllFaculties();
        assertEquals(2, allFaculties.size());
    }

    @Test
    public void testFindFacultiesByColor() {
        Faculty faculty1 = new Faculty();
        faculty1.setColor("red");
        facultyService.addFaculty(faculty1);
        Faculty faculty2 = new Faculty();
        faculty2.setColor("blue");
        facultyService.addFaculty(faculty2);
        List<Faculty> facultiesByColor = facultyService.findFacultiesByColor("red");
        assertEquals(1, facultiesByColor.size());
    }
}

