package ru.hogwarts.school.services;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exceptions.NotFoundException;
import ru.hogwarts.school.models.Avatar;
import ru.hogwarts.school.models.Student;
import ru.hogwarts.school.repositories.AvatarRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarService {
    private final AvatarRepository avatars;
    private final StudentService studentService;

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public AvatarService(AvatarRepository avatars, StudentService studentService) {
        this.avatars = avatars;
        this.studentService = studentService;
    }

    public Long uploadAvatar(Long studentId, @NotNull MultipartFile avatarFile) throws IOException {
        Student student = studentService.getStudent(studentId);
        Path filePath = Path.of(avatarsDir,
                studentId + "." + getExtensions(Objects.requireNonNull(avatarFile.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatarOrNew(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        Avatar saved = avatars.save(avatar);
        return saved.getId();
    }

    public Avatar findAvatarOrNew(Long studentId) {
        studentService.checkExistsId(studentId);
        return avatars.findByStudentId(studentId)
                .orElse(new Avatar());
    }

    public Avatar findAvatarOrFail(Long studentId) {
        studentService.checkExistsId(studentId);
        return avatars.findByStudentId(studentId)
                .orElseThrow(() -> new NotFoundException(
                                String.format("Avatar for student with id %d not found", studentId)
                        )
                );
    }

    private String getExtensions(@NotNull String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
