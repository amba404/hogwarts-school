package ru.hogwarts.school.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exceptions.NotFoundException;
import ru.hogwarts.school.models.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private FacultyService facultyService;

    @Test
    public void testAddFaculty() {
        Faculty faculty = new Faculty();

        when(facultyRepository.save(faculty)).thenReturn(faculty);

        Faculty addedFaculty = facultyService.addFaculty(faculty);

        assertNotNull(addedFaculty);
        verify(facultyRepository, times(1)).save(faculty);
    }

    @Test
    public void testGetFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);

        when(facultyRepository.existsById(1L)).thenReturn(true);
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        Faculty retrievedFaculty = facultyService.getFaculty(1L);

        assertNotNull(retrievedFaculty);
        assertEquals(1L, retrievedFaculty.getId());
        verify(facultyRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetFacultyNotFound() {
        long notFoundId = 111111L;

        when(facultyRepository.existsById(notFoundId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> facultyService.getFaculty(notFoundId));
        verify(facultyRepository, times(0)).findById(notFoundId);
    }

    @Test
    public void testUpdateFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);

        when(facultyRepository.existsById(1L)).thenReturn(true);
        when(facultyRepository.save(faculty)).thenReturn(faculty);

        Faculty updatedFaculty = facultyService.updateFaculty(faculty);

        assertNotNull(updatedFaculty);
        assertEquals(1L, updatedFaculty.getId());
        verify(facultyRepository, times(1)).existsById(1L);
        verify(facultyRepository, times(1)).save(faculty);
    }

    @Test
    public void testUpdateFacultyNotFound() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);

        when(facultyRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> facultyService.updateFaculty(faculty));
        verify(facultyRepository, times(1)).existsById(1L);
    }

    @Test
    public void testDeleteFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);

        when(facultyRepository.existsById(1L)).thenReturn(true);
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        Mockito.doNothing().when(facultyRepository).delete(faculty);

        Faculty deletedFaculty = facultyService.deleteFaculty(1L);

        assertNotNull(deletedFaculty);
        assertEquals(1L, deletedFaculty.getId());
        verify(facultyRepository, times(1)).findById(1L);
        verify(facultyRepository, times(1)).delete(faculty);
    }

    @Test
    public void testDeleteFacultyNotFound() {
        when(facultyRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> facultyService.deleteFaculty(1L));
        verify(facultyRepository, times(1)).existsById(1L);
    }

    @Test
    public void testGetAllFaculties() {
        Faculty faculty1 = new Faculty();
        Faculty faculty2 = new Faculty();

        when(facultyRepository.findAll()).thenReturn(List.of(faculty1, faculty2));

        Collection<Faculty> allFaculties = facultyService.getAllFaculties();

        assertNotNull(allFaculties);
        assertEquals(2, allFaculties.size());
        verify(facultyRepository, times(1)).findAll();
    }

    @Test
    public void testFindFacultiesByColor() {
        Faculty faculty1 = new Faculty();
        faculty1.setColor("red");
        Faculty faculty2 = new Faculty();
        faculty2.setColor("red");

        when(facultyRepository.findByColorIgnoreCase("red")).thenReturn(List.of(faculty1, faculty2));

        List<Faculty> facultiesByColor = facultyService.findFacultiesByColor("red");

        assertNotNull(facultiesByColor);
        assertEquals(2, facultiesByColor.size());
        verify(facultyRepository, times(1)).findByColorIgnoreCase("red");
    }

    @Test
    void findFacultiesByName() {
        Faculty faculty1 = new Faculty();
        faculty1.setName("faculty");

        when(facultyRepository.findByNameIgnoreCase("faculty")).thenReturn(List.of(faculty1));

        List<Faculty> facultiesByName = facultyService.findFacultiesByName("faculty");

        assertNotNull(facultiesByName);
        assertEquals(1, facultiesByName.size());
        verify(facultyRepository, times(1)).findByNameIgnoreCase("faculty");
    }

    @Test
    void findFacultyByStudentId() {
    }
}


