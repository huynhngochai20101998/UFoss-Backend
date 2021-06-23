package com.smartdev.ufoss.service;

import com.smartdev.ufoss.dto.SecurityDTO.ApplicationUser;
import com.smartdev.ufoss.entity.ConfirmationToken;
import com.smartdev.ufoss.entity.UserEntity;
import com.smartdev.ufoss.model.SecurityModel.ApplicationUserDao;
import com.smartdev.ufoss.repository.SecurityRepository.ApplicationUserRepository;
import com.smartdev.ufoss.repository.SecurityRepository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public ApplicationUserService(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder, ConfirmationTokenService confirmationTokenService, ConfirmationTokenRepository confirmationTokenRepository) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = applicationUserRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("can't find username: %s" + username));
        ApplicationUser applicationUser = new ApplicationUser(
                userEntity.getUserName(),
                userEntity.getPassword(),
                userEntity.getEmail(),
                userEntity.getApplicationUserRole().getGrantedAuthorities(),
                userEntity.getEnabled()
        );
        return applicationUser;
    }

    @Transactional
    public String signUpUser(UserEntity userEntity) {
        boolean usernameExists = applicationUserRepository.findByUsername(userEntity.getUserName()).isPresent();
        boolean emailExists = applicationUserRepository.findByEmail(userEntity.getEmail()).isPresent();

        if (usernameExists || emailExists) {
            UserEntity userFinding = usernameExists ? applicationUserRepository
                    .findByUsername(userEntity.getUserName()).get()
                    : applicationUserRepository.findByEmail(userEntity.getEmail()).get();
            if (userFinding.getEnabled())
                throw new IllegalStateException("email or username already taken!");
            else {
                ConfirmationToken confirmationToken = confirmationTokenRepository
                        .findByEmail(userFinding.getEmail())
                        .get();
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime createAtTime = confirmationToken.getCreateAt();

                if (now.isAfter(createAtTime.plusMinutes(1))){
                    confirmationTokenRepository.delete(confirmationToken);
                    userEntity = userFinding;
                }
                else throw new IllegalStateException(
                        "Please wait for 1 minute before get another verify again!"
                );
            }
        }
        else {
            String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
            userEntity.setPassword(encodedPassword);
            applicationUserRepository.save(userEntity);
        }

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),
                userEntity
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public int enableApplicationUser(String email) {
        return applicationUserRepository.enableUser(email);
    }
}
