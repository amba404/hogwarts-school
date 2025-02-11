package ru.hogwarts.school.services;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.NotFoundException;
import ru.hogwarts.school.models.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository students;

    public StudentService(StudentRepository students) {
        this.students = students;
    }

    public Student addStudent(Student student) {
        return students.save(student);
    }

    public Student getStudent(long id) {
        checkExistsId(id);
        return students.findById(id).get();
    }

    public Student updateStudent(Student student) {
        checkExistsId(student.getId());
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

    public Collection<Student> getAllStudents() {
        return students.findAll();
    }

    public List<Student> findStudentsByAge(int age) {
        return students.findByAge(age);
    }
}
