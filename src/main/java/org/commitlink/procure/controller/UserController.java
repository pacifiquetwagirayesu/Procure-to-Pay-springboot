package org.commitlink.procure.controller;

import static org.commitlink.procure.utils.Constants.ID;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.commitlink.procure.dto.UserEntityResponse;
import org.commitlink.procure.dto.UserListPagination;
import org.commitlink.procure.dto.UserRegisterRequest;
import org.commitlink.procure.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Endpoints", description = "User registration, retrieve endpoints")
@RequiredArgsConstructor
public class UserController {

  private final IUserService userService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public Map<String, Long> userRegister(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
    return Map.of(ID, userService.userRegister(userRegisterRequest));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public UserListPagination getUserList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
    return userService.getUserList(page, size);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public UserEntityResponse getUserById(@PathVariable long id) {
    return userService.getUserById(id);
  }
}
