package com.sw1project.profilemanagement.service;

import com.sw1project.profilemanagement.dto.CreateCreditCardRequest;
import com.sw1project.profilemanagement.dto.CreateUserRequest;
import com.sw1project.profilemanagement.dto.CreditCardResponse;
import com.sw1project.profilemanagement.dto.UpdateUserRequest;
import com.sw1project.profilemanagement.dto.UserResponse;
import com.sw1project.profilemanagement.entity.CreditCard;
import com.sw1project.profilemanagement.entity.User;
import com.sw1project.profilemanagement.repository.CreditCardRepository;
import com.sw1project.profilemanagement.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CreditCardRepository creditCardRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, CreditCardRepository creditCardRepository) {
        this.userRepository = userRepository;
        this.creditCardRepository = creditCardRepository;
    }

    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setHomeAddress(request.getHomeAddress());

        User saved = userRepository.save(user);

        return new UserResponse(
                saved.getUsername(),
                saved.getName(),
                saved.getEmail(),
                saved.getHomeAddress()
        );
    }

    public UserResponse getUser(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );

        return new UserResponse(
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getHomeAddress()
        );
    }

    public UserResponse updateUser(String username, UpdateUserRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email cannot be updated");
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        if (request.getHomeAddress() != null && !request.getHomeAddress().isBlank()) {
            user.setHomeAddress(request.getHomeAddress());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        User updated = userRepository.save(user);

        return new UserResponse(
                updated.getUsername(),
                updated.getName(),
                updated.getEmail(),
                updated.getHomeAddress()
        );
    }

    public CreditCardResponse addCreditCard(String username, CreateCreditCardRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );

        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber(request.getCardNumber());
        creditCard.setExpirationDate(request.getExpirationDate());
        creditCard.setCardHolderName(request.getCardHolderName());
        creditCard.setUser(user);

        CreditCard saved = creditCardRepository.save(creditCard);

        return new CreditCardResponse(
                saved.getId(),
                saved.getCardNumber(),
                saved.getExpirationDate(),
                saved.getCardHolderName(),
                user.getUsername()
        );
    }
}