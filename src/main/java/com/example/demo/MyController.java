package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataAccessException;

import java.util.*;

@RestController
public class MyController {

    @Autowired
    private UserDataRepository userDataRepository;

    @PostMapping("/addUser")
    public ResponseEntity<Map<String, String>> addUser(@RequestBody UserData userData) {
        Map<String, String> response = new LinkedHashMap<>();
        try {
            userDataRepository.save(userData);
            response.put("status", "ok");
            response.put("message", "User added successfully");
            return ResponseEntity.ok(response);
        } catch (DataAccessException e) {
            response.put("status", "error");
            response.put("message", "Failed to add user");
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/users")
    public List<UserData> getUsers() {
        return userDataRepository.findAll();
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<Map<String, String>> editUser(@PathVariable int userId, @RequestBody UserData updatedUserData) {
        Optional<UserData> userOptional = userDataRepository.findById(userId);

        if (userOptional.isPresent()) {
            UserData user = userOptional.get();
            user.setUsername(updatedUserData.getUsername());
            user.setEmail(updatedUserData.getEmail());
            userDataRepository.save(user);

            Map<String, String> response = new LinkedHashMap<>();
            response.put("status", "ok");
            response.put("message", "User updated successfully");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable int userId) {
        Optional<UserData> userOptional = userDataRepository.findById(userId);

        if (userOptional.isPresent()) {
            userDataRepository.delete(userOptional.get());
            Map<String, String> response = new LinkedHashMap<>();
            response.put("status", "ok");
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}