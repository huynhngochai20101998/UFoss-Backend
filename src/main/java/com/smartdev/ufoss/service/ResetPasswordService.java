package com.smartdev.ufoss.service;

import com.smartdev.ufoss.dto.UserResetPassDTO;
import com.smartdev.ufoss.entity.UserEntity;

public interface ResetPasswordService {
    public UserEntity checkEmailExists(String username, String email);

    public String updatePassword(String username, String password);
}
