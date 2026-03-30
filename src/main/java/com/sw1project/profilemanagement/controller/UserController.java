package com.sw1project.profilemanagement.controller;

import com.sw1project.profilemanagement.dto.CreateCreditCardRequest;
import com.sw1project.profilemanagement.dto.CreateUserRequest;
import com.sw1project.profilemanagement.dto.CreditCardResponse;
import com.sw1project.profilemanagement.dto.UpdateUserRequest;
import com.sw1project.profilemanagement.dto.UserResponse;
import com.sw1project.profilemanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{username}")
    public UserResponse getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @PutMapping("/{username}")
    public UserResponse updateUser(@PathVariable String username,
                                   @RequestBody UpdateUserRequest request) {
        return userService.updateUser(username, request);
    }

    @PostMapping("/{username}/credit-cards")
    public ResponseEntity<CreditCardResponse> addCreditCard(@PathVariable String username,
                                                            @Valid @RequestBody CreateCreditCardRequest request) {
        CreditCardResponse created = userService.addCreditCard(username, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}