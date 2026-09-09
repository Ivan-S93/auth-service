package com.loginhgco.auth_service.controller;

import com.loginhgco.auth_service.models.User;
import com.loginhgco.auth_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // GET /api/users -> Carga los usuarios en la tabla de React
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // PATCH /api/users/{id}/status -> Activa o desactiva al usuario
    @PatchMapping("/{id}/status")
    public ResponseEntity<User> toggleUserStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> statusUpdate) {
        
        return userRepository.findById(id)
                .map(user -> {
                    Boolean active = statusUpdate.get("active");
                    if (active != null) {
                        user.setActive(active);
                    }
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
            }
}