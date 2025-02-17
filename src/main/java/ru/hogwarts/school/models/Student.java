package ru.hogwarts.school.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class Student {

    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Имя должно быть заполнено")
    private String name;

    @Min(value = 10, message = "Возраст должен быть от 10 до 120")
    @Max(value = 120, message = "Возраст должен быть от 10 до 120")
    private int age;

    @NotNull(message = "Факультет должен быть заполнен")
    @ManyToOne(targetEntity = Faculty.class)
    @JoinColumn(name = "faculty_id", nullable = false)
//    @JsonManagedReference
    private Faculty faculty;

}
