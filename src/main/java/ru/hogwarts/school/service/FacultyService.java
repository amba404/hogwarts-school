package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Service
public class FacultyService {

    private final HashMap<Long, Faculty> faculties = new HashMap<>();
    private Long lastId = 0L;


    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(++lastId);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty getFaculty(long id) {
        checkExistsId(id);
        return faculties.get(id);
    }

    public Faculty updateFaculty(Faculty faculty) {
        checkExistsId(faculty.getId());
        return faculties.put(faculty.getId(), faculty);
    }

    public Faculty deleteFaculty(long id) {
        checkExistsId(id);
        return faculties.remove(id);
    }

    private void checkExistsId(long id) {
        if (!faculties.containsKey(id)) {
            throw new NotFoundException(String.format("Faculty with id %d not found", id));
        }
     }

    public Collection<Faculty> getAllFaculties() {
        return faculties.values();
    }

    public List<Faculty> findFacultiesByColor(String color) {
        return faculties.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(java.util.stream.Collectors.toList());
    }
}
