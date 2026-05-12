package com.edifika.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EdifikaMicroserviceIamApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdifikaMicroserviceIamApplication.class, args);
    }

}
