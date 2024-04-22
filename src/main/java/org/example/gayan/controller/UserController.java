package org.example.gayan.controller;

import lombok.RequiredArgsConstructor;
import org.example.gayan.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/v1/users")
public class UserController {


    @GetMapping(value = "/")
    @PreAuthorize("hasAnyRole('CLIENT_USER','GAYAN')")
    ResponseEntity<List<UserDto>> getAllUsers(){
        UserDto build = UserDto.builder().id(System.currentTimeMillis())
                .salary(System.nanoTime())
                .lastName("last_name_"+System.currentTimeMillis())
                .firstName("first_name_"+System.currentTimeMillis())
                .build();

        return ResponseEntity.ok(Collections.singletonList(build));
    }
}
