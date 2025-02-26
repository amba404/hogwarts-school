package ru.hogwarts.school.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
public class Avatar {
    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "Путь не может быть пустым")
    @JsonIgnore
    private String filePath;

    @NotEmpty
    private long fileSize;

    @NotEmpty(message = "Тип файла не может быть пустым")
    private String mediaType;

    @NotEmpty
    @Lob
    @JsonIgnore
    private byte[] data;

    @NotNull
    @OneToOne
    private Student student;
}
