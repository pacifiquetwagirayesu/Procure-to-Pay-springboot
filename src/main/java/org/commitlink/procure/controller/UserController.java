package org.commitlink.procure.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.commitlink.procure.dto.UserRegisterRequest;
import org.commitlink.procure.services.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Endpoints", description = "User registration, retrieve endpoints")
@AllArgsConstructor
public class UserController {
    private final IUserService userService;

    @PostMapping("/register")
    public long userRegister(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        return userService.userRegister(userRegisterRequest);
    }
}
