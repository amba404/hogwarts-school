package ru.hogwarts.school.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.models.Avatar;
import ru.hogwarts.school.models.Student;

import java.util.Optional;

@Transactional
public interface AvatarRepository extends JpaRepository<Avatar, Integer> {
    Optional<Avatar> findByStudentId(Long studentId);
}
