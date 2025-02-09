package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Test
    public void testAddStudent() {
        Student student = new Student();
        student.setAge(20);
        Student addedStudent = studentService.addStudent(student);
        assertNotNull(addedStudent);
        assertEquals(1L, addedStudent.getId());
    }

    @Test
    public void testGetStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setAge(20);
        studentService.addStudent(student);
        Student retrievedStudent = studentService.getStudent(1L);
        assertNotNull(retrievedStudent);
        assertEquals(1L, retrievedStudent.getId());
    }

    @Test
    public void testUpdateStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setAge(20);

        studentService.addStudent(student);
        student.setAge(21);

        Student updatedStudent = studentService.updateStudent(student);

        assertNotNull(updatedStudent);
        assertEquals(21, updatedStudent.getAge());

        student.setId(222L);
        updatedStudent = studentService.updateStudent(student);
        assertNull(updatedStudent);
    }

    @Test
    public void testDeleteStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setAge(20);
        studentService.addStudent(student);

        Student deletedStudent = studentService.deleteStudent(1L);

        assertNotNull(deletedStudent);
        assertEquals(1L, deletedStudent.getId());

        deletedStudent = studentService.deleteStudent(222L);
        assertNull(deletedStudent);

    }

    @Test
    public void testGetAllStudents() {
        Student student1 = new Student();
        student1.setAge(20);
        studentService.addStudent(student1);
        Student student2 = new Student();
        student2.setAge(21);
        studentService.addStudent(student2);
        Collection<Student> allStudents = studentService.getAllStudents();
        assertEquals(2, allStudents.size());
    }

    @Test
    public void testFindStudentsByAge() {
        Student student1 = new Student();
        student1.setAge(20);
        studentService.addStudent(student1);
        Student student2 = new Student();
        student2.setAge(21);
        studentService.addStudent(student2);
        List<Student> studentsByAge = studentService.findStudentsByAge(20);
        assertEquals(1, studentsByAge.size());
    }
}
