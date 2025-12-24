package org.commitlink.procure.controller;

import static org.commitlink.procure.utils.Constants.ID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.commitlink.procure.dto.user.UserEntityResponse;
import org.commitlink.procure.dto.user.UserListPaginationResponse;
import org.commitlink.procure.dto.user.UserRegisterRequest;
import org.commitlink.procure.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  @Operation(summary = "Register a user request")
  public Map<String, Long> userRegister(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
    return Map.of(ID, userService.userRegister(userRegisterRequest));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "User list request")
  public UserListPaginationResponse getUserList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
    return userService.getUserList(page, size);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get a user by id request")
  public UserEntityResponse getUserById(@PathVariable long id) {
    return userService.getUserById(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete user request")
  public void deleteUserById(@PathVariable long id) {
    userService.deleteUserById(id);
  }
}
