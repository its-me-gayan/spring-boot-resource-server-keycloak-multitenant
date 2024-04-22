package org.example.gayan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableMethodSecurity
public class SpringBootResourceServerRunner
{
    public static void main(String[] args) {
        SpringApplication.run(SpringBootResourceServerRunner.class, args);
    }

}
