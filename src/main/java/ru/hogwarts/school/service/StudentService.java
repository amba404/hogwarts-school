package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Service
public class StudentService {

    private final HashMap<Long, Student> students = new HashMap<>();
    private Long lastId = 0L;


    public Student addStudent(Student student) {
        student.setId(++lastId);
        students.put(student.getId(), student);
        return student;
    }

    public Student getStudent(long id) {
        checkExistsId(id);
        return students.get(id);
    }

    public Student updateStudent(Student student) {
        checkExistsId(student.getId());
        return students.put(student.getId(), student);
    }

    public Student deleteStudent(long id) {
        checkExistsId(id);
        return students.remove(id);
    }

    private void checkExistsId(long id) {
        if (!students.containsKey(id)) {
            throw new NotFoundException("Student with id " + id + " not found");
        }
    }

    public Collection<Student> getAllStudents() {
        return students.values();
    }

    public List<Student> findStudentsByAge(int age) {
        return students.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(java.util.stream.Collectors.toList());
    }
}
