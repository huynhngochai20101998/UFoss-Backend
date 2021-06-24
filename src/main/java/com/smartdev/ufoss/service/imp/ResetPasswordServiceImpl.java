package com.smartdev.ufoss.service.imp;

import com.smartdev.ufoss.config.SecurityConfig.SecurityConfig;
import com.smartdev.ufoss.dto.UserResetPassDTO;
import com.smartdev.ufoss.entity.UserEntity;
import com.smartdev.ufoss.repository.UserRepository;
import com.smartdev.ufoss.service.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    public UserEntity checkEmailExists(String username, String email) {

       return  userRepository.findByUserNameAndEmail(username, email);

    }

    @Override
    public String updatePassword(String username, String password) {


        Optional<UserEntity> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            user.get().setPassword(securityConfig.passwordEncoder().encode(password));
            userRepository.save(user.get());
        }

        return "ok";
    }
}
