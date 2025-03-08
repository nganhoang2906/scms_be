package com.scms.scms_be.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scms.scms_be.config.JWTUntils;
import com.scms.scms_be.model.dto.UserDto;
import com.scms.scms_be.model.entity.User;
import com.scms.scms_be.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository usersRepo;

    @Autowired
    private JWTUntils jwtUntils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;


    public UserDto getAllUsers() {
        UserDto response = new UserDto();
        try {
            List<User> result = usersRepo.findAll();
            if (!result.isEmpty()) {
                response.setOurUsersList(result);
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                response.setStatusCode(404);
                response.setMessage("Not found user");
            }
            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error occurred " + e.getMessage());
            return response;
        }
    }

    public UserDto getUserById(long id) {
        UserDto response = new UserDto();
        try {
            User userbyId = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            response.setOurUsers(userbyId);
            response.setStatusCode(200);
            response.setMessage("Found user with id: " + id + " successfully");
            return response;

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error occurred " + e.getMessage());
            return response;
        }
    }

    public UserDto deleteUser(Long userid) {
        UserDto response = new UserDto();
        try {
            Optional<User> userOptional = usersRepo.findById(userid);
            if (userOptional.isPresent()) {
                usersRepo.deleteById(userid);
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                response.setStatusCode(404);
                response.setMessage("Not found user for delete");
            }
            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error to delete user " + e.getMessage());
            return response;
        }
    }

    public UserDto updateUser(Long userid, User updateUser) {
        UserDto response = new UserDto();
        try {
            Optional<User> userOptional = usersRepo.findById(userid);
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();
                existingUser.setUsername(updateUser.getUsername());
                existingUser.setEmail(updateUser.getEmail());
                existingUser.setRole(updateUser.getRole());
                existingUser.setStatus(updateUser.getStatus());

            // Fix: Check if the password is not null and not empty
            if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
            }


                User saveOurUser = usersRepo.save(existingUser);
                response.setOurUsers(saveOurUser);
                response.setStatusCode(200);
                response.setMessage("User Update Successful");
            } else {
                response.setStatusCode(404);
                response.setMessage("Not found user for Update");
            }
            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error to update user " + e.getMessage());
            return response;
        }
    }

    public UserDto getMyInfo(String email) {
        UserDto response = new UserDto();
        try {
            Optional<User> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                response.setOurUsers(userOptional.get());
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                response.setStatusCode(404);
                response.setMessage("Not found user");
            }
            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error to get user Info " + e.getMessage());
            return response;
        }
    }

}
