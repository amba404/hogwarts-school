package ru.hogwarts.school.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.NotFoundException;
import ru.hogwarts.school.models.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository faculties;

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository faculties) {
        this.faculties = faculties;
    }


    public Faculty addFaculty(Faculty faculty) {
        logger.info("addFaculty: Faculty add");

        faculty.setId(null);
        return faculties.save(faculty);
    }

    public Faculty getFaculty(long id) {
        logger.info("getFaculty: Faculty get");

        checkExistsId(id);
        return faculties.findById(id).get();
    }

    public Faculty updateFaculty(Faculty faculty) {
        logger.info("updateFaculty: Faculty update");

        checkExistsId(faculty.getId());
        return faculties.save(faculty);
    }

    public Faculty deleteFaculty(long id) {
        logger.info("deleteFaculty: Faculty delete");

        Faculty faculty = getFaculty(id);
        faculties.delete(faculty);
        return faculty;
    }

    private void checkExistsId(long id) {
        if (!faculties.existsById(id)) {
            logger.error("checkExistsId: Faculty not found {}", id);

            throw new NotFoundException(String.format("Faculty with id %d not found", id));
        }
    }

    public List<Faculty> getAllFaculties() {
        return faculties.findAll();
    }

    public List<Faculty> findFacultiesByColor(String color) {
        return faculties.findByColorIgnoreCase(color);
    }

    public List<Faculty> findFacultiesByName(String name) {
        return faculties.findByNameIgnoreCase(name);
    }

    public List<Faculty> findFacultyByStudentId(Long id) {
        return faculties.findFacultyByStudentsId(id);
    }
}
