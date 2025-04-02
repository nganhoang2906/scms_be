package com.scms.scms_be.service.General;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scms.scms_be.model.dto.UserDto;
import com.scms.scms_be.model.entity.General.User;
import com.scms.scms_be.repository.General.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository usersRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public UserDto getUserByEmployeeId(Long employeeId) {
        UserDto response = new UserDto();
        try {
            List<User> result = usersRepo.findByEmployeeEmployeeId(employeeId);
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
            response.setOurUser(userbyId);
            response.setStatusCode(200);
            response.setMessage("Found user with id: " + id + " successfully");
            return response;

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error occurred " + e.getMessage());
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

                if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
                    existingUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
                }

                User saveOurUser = usersRepo.save(existingUser);
                response.setOurUser(saveOurUser);
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

    public UserDto getMyInfo(String username) {
        UserDto response = new UserDto();
        try {
            Optional<User> userOptional = usersRepo.findByUsername(username);
            if (userOptional.isPresent()) {
                response.setOurUser(userOptional.get());
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
