package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;

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
        return students.get(id);
    }

    public Student updateStudent(Student student) {
        if (!students.containsKey(student.getId())){
            return null;
        }
        return students.put(student.getId(), student)  ;
    }

    public Student deleteStudent(long id) {
        if (!students.containsKey(id)){
            return null;
        }
        return students.remove(id);
    }

    public Collection<Student> getAllStudents() {
        return students.values();
    }

    public Collection<Student> findStudentsByAge(int age) {
       return students.values().stream()
               .filter(student -> student.getAge() == age)
               .collect(java.util.stream.Collectors.toList());
    }
}
