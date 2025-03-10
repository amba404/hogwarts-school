package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.models.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int min, int max);

    List<Student> findStudentsByFacultyId(Long facultyId);

    @Query(value = "select count(*) as cnt from Student", nativeQuery = true)
    Integer getCountOfStudents();

    @Query(value = "select avg(age) as avg from Student", nativeQuery = true)
    Integer getAvgAgeOfStudents();

    @Query(value = "select * from Student order by id desc limit 5", nativeQuery = true)
    List<Student> getLastFiveStudents();
}
