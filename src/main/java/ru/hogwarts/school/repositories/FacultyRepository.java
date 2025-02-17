package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.models.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findByColorIgnoreCase(String color);

    List<Faculty> findByNameIgnoreCase(String name);

    //@Query("select f from Faculty f inner join Student s on f=s.faculty where s.id = ?1")
    List<Faculty> findFacultyByStudentsId(Long id);
}
