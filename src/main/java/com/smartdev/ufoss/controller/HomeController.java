package com.smartdev.ufoss.controller;

import com.smartdev.ufoss.service.UserService;
import com.smartdev.ufoss.service.impI.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
public class HomeController {

    private UserService userService;

    @Autowired
    public HomeController(UserServiceImpl userService) {
        this.userService = userService;
    }

    // default homePage ---> Get Category && Course to Home Page here.

    //
}
