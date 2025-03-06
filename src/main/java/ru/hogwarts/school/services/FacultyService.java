package ru.hogwarts.school.services;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.NotFoundException;
import ru.hogwarts.school.models.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;
import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository faculties;

    public FacultyService(FacultyRepository faculties) {
        this.faculties = faculties;
    }


    public Faculty addFaculty(Faculty faculty) {
        return faculties.save(faculty);
    }

    public Faculty getFaculty(long id) {
        checkExistsId(id);
        return faculties.findById(id).get();
    }

    public Faculty updateFaculty(Faculty faculty) {
        checkExistsId(faculty.getId());
        return faculties.save(faculty);
    }

    public Faculty deleteFaculty(long id) {
        Faculty faculty = getFaculty(id);
        faculties.delete(faculty);
        return faculty;
    }

    private void checkExistsId(long id) {
        if (!faculties.existsById(id)) {
            throw new NotFoundException(String.format("Faculty with id %d not found", id));
        }
    }

    public Collection<Faculty> getAllFaculties() {
        return faculties.findAll();
    }

    public List<Faculty> findFacultiesByColor(String color) {
        return faculties.findByColor(color);
    }
}
