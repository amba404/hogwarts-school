package ru.hogwarts.school.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.hogwarts.school.exceptions.NotFoundException;
import ru.hogwarts.school.models.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StudentServiceTest {

    private final Long notFoundId = 111111L;
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;
    private Student student1, student2;

    @BeforeEach
    void setUp() {
        student1 = new Student(1L, "Ivan", 10);
        student2 = new Student(2L, "Petr", 10);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        when(studentRepository.findById(2L)).thenReturn(Optional.of(student2));
        when(studentRepository.existsById(1L)).thenReturn(true);
        when(studentRepository.existsById(2L)).thenReturn(true);
        when(studentRepository.existsById(notFoundId)).thenReturn(false);
        when(studentRepository.save(student1)).thenReturn(student1);
        when(studentRepository.save(student2)).thenReturn(student2);
        when(studentRepository.findAll()).thenReturn(List.of(student1, student2));
        when(studentRepository.findByAge(10)).thenReturn(List.of(student1, student2));

    }

    @Test
    public void testAddStudent() {
        Student addedStudent = studentService.addStudent(student1);

        assertNotNull(addedStudent);
        verify(studentRepository, times(1)).save(student1);
    }

    @Test
    public void testGetStudent() {
        Student retrievedStudent = studentService.getStudent(1L);

        assertNotNull(retrievedStudent);
        assertEquals(student1, retrievedStudent);
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetStudentNotFound() {
        assertThrows(NotFoundException.class, () -> studentService.getStudent(notFoundId));
    }

    @Test
    public void testUpdateStudent() {
        Student updatedStudent = studentService.updateStudent(student1);

        assertNotNull(updatedStudent);

        assertEquals(1L, updatedStudent.getId());
        verify(studentRepository, times(1)).existsById(1L);
        verify(studentRepository, times(1)).save(student1);
    }

    @Test
    public void testUpdateStudentNotFound() {
        Student student = new Student();
        student.setId(notFoundId);

        assertThrows(NotFoundException.class, () -> studentService.updateStudent(student));
    }

    @Test
    public void testDeleteStudent() {
        Student deletedStudent = studentService.deleteStudent(1L);

        assertNotNull(deletedStudent);
        assertEquals(1L, deletedStudent.getId());

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).delete(student1);
    }

    @Test
    public void testDeleteStudentNotFound() {
        assertThrows(NotFoundException.class, () -> studentService.deleteStudent(notFoundId));
    }

    @Test
    public void testGetAllStudents() {
        Collection<Student> allStudents = studentService.getAllStudents();

        assertNotNull(allStudents);
        assertEquals(2, allStudents.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    public void testFindStudentsByAge() {
        List<Student> studentsByAge = studentService.findStudentsByAge(10);

        assertNotNull(studentsByAge);
        assertEquals(2, studentsByAge.size());
        verify(studentRepository, times(1)).findByAge(10);
    }
}


