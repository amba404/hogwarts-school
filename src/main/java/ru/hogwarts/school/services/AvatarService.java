package ru.hogwarts.school.services;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exceptions.NotFoundException;
import ru.hogwarts.school.models.Avatar;
import ru.hogwarts.school.models.Student;
import ru.hogwarts.school.repositories.AvatarRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;
    @Value(value = "${path.to.avatars}")
    private String avatarDirectory;

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    public Long uploadAvatar(Long studentId, @NotNull MultipartFile mFile) throws IOException {
        logger.info("uploadAvatar: Upload avatar for student {}", studentId);

        Student student = studentService.getStudent(studentId);

        Path filePath = Path.of(avatarDirectory, studentId + "." + getExtension(mFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = mFile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = findAvatarOrNew(studentId);
        avatar.setStudent(student);
        avatar.setMediaType(mFile.getContentType());
        avatar.setFileSize(mFile.getSize());
        avatar.setData(mFile.getBytes());
        avatar.setFilePath(filePath.toString());

        return avatarRepository.save(avatar).getId();
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Avatar findAvatarOrNew(Long studentId) {
        logger.info("findAvatarOrNew: Find avatar for student or create new {}", studentId);

        return avatarRepository.findByStudentId(studentId)
                .orElseGet(Avatar::new);
    }

    public Avatar findAvatarOrFail(Long studentId) {
        logger.info("findAvatarOrNew: Find avatar for student or throws exception {}", studentId);

        Optional<Avatar> byStudentId = avatarRepository.findByStudentId(studentId);

        if (byStudentId.isEmpty()) {
            logger.error("findAvatarOrNew: avatar for student {} not found", studentId);
            throw new NotFoundException(String.format("Avatar for student %d not found", studentId));
        }

        return byStudentId.get();
    }

    public List<Avatar> getAllPaged(int pageNumber, int pageSize) {
        logger.info("getAllPaged: Get all avatars, page {}, size {}", pageNumber, pageSize);

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}
