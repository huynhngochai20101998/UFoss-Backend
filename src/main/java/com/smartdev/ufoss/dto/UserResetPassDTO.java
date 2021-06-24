package com.smartdev.ufoss.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResetPassDTO {
    private String userName;
    private String password;
    private String email;
}
