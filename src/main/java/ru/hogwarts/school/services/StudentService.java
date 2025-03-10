package ru.hogwarts.school.services;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.NotFoundException;
import ru.hogwarts.school.models.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository students;
    private final FacultyService faculties;

    public StudentService(StudentRepository students, FacultyService faculties) {
        this.students = students;
        this.faculties = faculties;
    }

    public Student addStudent(Student student) {
        student.setId(null);
        reSetFaculty(student);
        return students.save(student);
    }

    private void reSetFaculty(Student student) {
        student.setFaculty(faculties.getFaculty(student.getFaculty().getId()));
    }

    public Student getStudent(long id) {
        checkExistsId(id);
        return students.findById(id).orElseThrow();
    }

    public Student updateStudent(Student student) {
        checkExistsId(student.getId());
        reSetFaculty(student);
        return students.save(student);
    }

    public Student deleteStudent(long id) {
        Student student = getStudent(id);
        students.delete(student);
        return student;
    }

    private void checkExistsId(long id) {
        if (!students.existsById(id)) {
            throw new NotFoundException("Student with id " + id + " not found");
        }
    }

    public List<Student> getAllStudents() {
        return students.findAll();
    }

    public List<Student> findStudentsByAge(int age) {
        return students.findByAge(age);
    }

    public List<Student> findStudentsByAge(int min, int max) {
        return students.findByAgeBetween(min, max);
    }

    public List<Student> findStudentsByFacultyId(Long facultyId) {
        return students.findStudentsByFacultyId(facultyId);
    }

    public Integer getCountOfStudents() {
        return students.getCountOfStudents();
    }

    public Integer getAvgAgeOfStudents() {
        return students.getAvgAgeOfStudents();
    }

    public List<Student> getLastFiveStudents() {
        return students.getLastFiveStudents();
    }
}
