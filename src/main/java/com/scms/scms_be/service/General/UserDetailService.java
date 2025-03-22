package com.scms.scms_be.service.General;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scms.scms_be.repository.General.UserRepository;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        System.out.println("DEBUG: Tìm kiếm User với email hoặc username: " + emailOrUsername);

        return userRepo.findByEmail(emailOrUsername)
                .or(() -> userRepo.findByUsername(emailOrUsername)) // Tìm cả username
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy User với email hoặc username: " + emailOrUsername));
    }



}
