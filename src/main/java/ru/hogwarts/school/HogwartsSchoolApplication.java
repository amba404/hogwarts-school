package ru.hogwarts.school;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(title = "Hogwarts School API", version = "1.0.0", description = "API for Hogwarts School"))
public class HogwartsSchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(HogwartsSchoolApplication.class, args);
    }

}
