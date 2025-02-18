package ru.hogwarts.school.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
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
    Long id;

    @NotBlank
    String filePath;

    @NotBlank
    long fileSize;

    @NotBlank
    String mediaType;

    @NotBlank
    byte[] data;

    @NotNull
    @OneToOne
    Student student;


}
