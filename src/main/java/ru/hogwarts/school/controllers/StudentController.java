package ru.hogwarts.school.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.models.Faculty;
import ru.hogwarts.school.models.Student;
import ru.hogwarts.school.services.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @GetMapping("/all")
    public Collection<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @GetMapping("/{id}/faculty")
    public Faculty getFaculty(@PathVariable Long id) {
        return studentService.getStudent(id).getFaculty();
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/{id}")
    public Student deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping("/find/age")
    public ResponseEntity<Collection<Student>> findStudentsByAge(@RequestParam(required = false) Integer age,
                                                                 @RequestParam(required = false) Integer min,
                                                                 @RequestParam(required = false) Integer max) {
        if (age != null) {
            return ResponseEntity.ok(studentService.findStudentsByAge(age));
        } else if (min != null && max != null) {
            return ResponseEntity.ok(studentService.findStudentsByAge(min, max));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/find/faculty/{id}")
    public Collection<Student> findStudentsByFacultyId(@PathVariable Long id) {
        return studentService.findStudentsByFacultyId(id);
    }

    @GetMapping("/get/count")
    public Integer getCountOfStudents() {
        return studentService.getCountOfStudents();
    }

    @GetMapping("/get/avg-age")
    public Integer getAvgAgeOfStudents() {
        return studentService.getAvgAgeOfStudents();
    }

    @GetMapping("/get/last-five")
    public Collection<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }
}
