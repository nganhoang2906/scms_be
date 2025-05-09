package com.scms.scms_be.repository.General;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.General.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  
  Optional<User> findByEmail(String email);

  Optional<User> findByEmailAndOtp(String email, String otp);

  Optional<User> findByUsername(String username);

  User findByEmployeeEmployeeId(Long employeeId);

  List<User> findByEmployee_Department_Company_CompanyId(Long companyId);
  
}
