package com.scms.scms_be.service.General;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.General.UserDto;
import com.scms.scms_be.model.dto.request.UpdatePasswordRequest;
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
        return users.stream().map(this::convertToDto).toList();
    }
    
    public List<UserDto>  getAllUsersByCompanyId(Long companyId) {
        List<User> users = usersRepo.findByEmployee_Department_Company_CompanyId(companyId);
        return users.stream().map(this::convertToDto).toList();
    }
    
    public UserDto getUserByEmployeeId(Long employeeId) {
        User users = usersRepo.findByEmployeeEmployeeId(employeeId);
        return convertToDto(users);
    }
    
    public UserDto getUserById(long id) {
        User user = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User không tồn tại"));
        return convertToDto(user);
    }

    public UserDto updateUser(Long userId, User updateUser) {
        User existingUser = usersRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        existingUser.setUsername(updateUser.getUsername());
        existingUser.setEmail(updateUser.getEmail());
        existingUser.setRole(updateUser.getRole());
        existingUser.setStatus(updateUser.getStatus());

        if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        }

        return convertToDto(usersRepo.save(existingUser));
    }

    public void updatePassword(Long userId, UpdatePasswordRequest request) {
        User user = usersRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
    
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new CustomException("Mật khẩu hiện tại không đúng", HttpStatus.BAD_REQUEST);
        }
    
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usersRepo.save(user);
    }    

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setEmployeeId(user.getEmployee().getEmployeeId());
        dto.setEmployeeCode(user.getEmployee().getEmployeeCode());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        return dto;
    }
    

}
