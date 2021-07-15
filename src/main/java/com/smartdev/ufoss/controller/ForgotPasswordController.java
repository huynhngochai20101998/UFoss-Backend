package com.smartdev.ufoss.controller;

import com.smartdev.ufoss.component.Validator;
import com.smartdev.ufoss.dto.ResetPasswordDTO;
import com.smartdev.ufoss.entity.UserEntity;
import com.smartdev.ufoss.exception.UserNotFoundException;
import com.smartdev.ufoss.service.EmailSenderService;
import com.smartdev.ufoss.service.UserService;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class ForgotPasswordController {

    private final UserService userService;
    private final EmailSenderService emailSenderService;


    @PostMapping("/reset-password")
    public ResponseEntity<?> processForgotPasswordForm(@RequestBody ResetPasswordDTO model) {

        if (!Validator.emailValidate(model.getEmail())) {
            return ResponseEntity.badRequest().body("Email not validate!");
        }

        String token = RandomString.make(50);

        try {
            UserEntity user = userService.updateResetPassword(token, model.getEmail());
            String subjectEmail = "Email <Reset Password>";

            String resetPasswordLink = "https://ufoss-smd-intern.herokuapp.com" + "/reset-password?token=" + token;

            emailSenderService.emailResetPassword(
                    user.getEmail(),
                    subjectEmail,
                    user.getLastName() + " " + user.getFirstName(),
                    resetPasswordLink,
                    "Click to Reset Password!"
            );

        } catch (UserNotFoundException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("check your email");
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> processResetPassword(@RequestBody ResetPasswordDTO model) {
        System.out.println(model.getPassword());
        UserEntity user = userService.getUserWithToken(model.getResetPasswordToken());

        if (user != null) {
            user.setPassword(model.getPassword());
            userService.updatePassword(user);

            return ResponseEntity.ok("Successfully");
        }

        return ResponseEntity.badRequest().body("Password update faild!");
    }
}
