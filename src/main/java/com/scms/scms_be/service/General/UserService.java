package com.scms.scms_be.service.General;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scms.scms_be.model.dto.General.UserDto;
import com.scms.scms_be.model.entity.General.User;
import com.scms.scms_be.repository.General.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository usersRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDto>  getAllUsers() {
        List<User> users = usersRepo.findAll();
        return users.stream().map(this::mapToDto).toList();
    }
    
    public List<UserDto>  getAllUsersByCompanyId(Long companyId) {
        List<User> users = usersRepo.findByEmployee_Department_Company_CompanyId(companyId);
        return users.stream().map(this::mapToDto).toList();
    }
    
    public UserDto getUserByEmployeeId(Long employeeId) {
        User users = usersRepo.findByEmployeeEmployeeId(employeeId);
        return mapToDto(users);
    }
    
    public UserDto getUserById(long id) {
        User user = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDto(user);
    }

    public UserDto updateUser(Long userId, User updateUser) {
        User existingUser = usersRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for update"));

        existingUser.setUsername(updateUser.getUsername());
        existingUser.setEmail(updateUser.getEmail());
        existingUser.setRole(updateUser.getRole());
        existingUser.setStatus(updateUser.getStatus());

        if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        }

        return mapToDto(usersRepo.save(existingUser));
    }

    public UserDto getMyInfo(String username) {
        User user = usersRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return mapToDto(user);
    }
    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setEmployeeId(user.getEmployee().getEmployeeId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        return dto;
    }
    

}
