package ru.hogwarts.school.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
public class Faculty {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Название должно быть заполнено")
    private String name;

    @NotNull(message = "Цвет должен быть заполнен")
    private String color;

    @JsonProperty(required = true)
    @OneToMany(mappedBy = "faculty", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Student> students;

    public Faculty() {
        this.id = 0L;
        this.name = "";
        this.color = "";
        this.students = Set.of();
    }

    public Faculty(Long id) {
        this();
        this.id = id;
    }

    public Faculty(@NotNull Long id, String name, String color, Collection<Student> students) {
        this(id);
        if (name == null || color == null || students == null) {
            return;
        }
        this.name = name;
        this.color = color;
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return Objects.equals(id, faculty.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", studentsCnt=" + students.size() +
                '}';
    }
}
