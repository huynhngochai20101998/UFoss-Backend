package com.smartdev.ufoss.controller;

import com.smartdev.ufoss.dto.UserResetPassDTO;
import com.smartdev.ufoss.entity.UserEntity;
import com.smartdev.ufoss.service.EmailSenderService;
import com.smartdev.ufoss.service.ResetPasswordService;
import com.smartdev.ufoss.service.imp.ResetPasswordServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping()
@AllArgsConstructor
public class LoginController {

    @Autowired
    private ResetPasswordService resetPassService;

    @Autowired
    private EmailSenderService emailSenderService;

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_USER')")
    @GetMapping()
    public String getLogin() {
        return "login";
    }

    @GetMapping("trywithtoken")
    public String tryWithToken() { return "trywithtoken"; }

    @GetMapping("/login")
    public Principal prevent(Principal principal)  {
        return principal;
    }

    @GetMapping("/pass-reset")
    public ResponseEntity<?> resetPassword(@RequestBody UserResetPassDTO reset) {
        UserEntity user = resetPassService.checkEmailExists(reset.getUserName(), reset.getEmail());

        if (user != null ) {
            emailSenderService.resetPassword("huynhngochai20101998@gmail.com");
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.ok("error");
    }

    @PostMapping("/pass-update")
    public ResponseEntity<?> updatePassword(@RequestBody UserResetPassDTO newUser) {
        String message = resetPassService.updatePassword(newUser.getUserName(), newUser.getPassword());
        return ResponseEntity.ok(message);
    }
}