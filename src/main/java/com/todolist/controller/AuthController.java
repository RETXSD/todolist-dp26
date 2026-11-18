package com.todolist.controller;

import com.todolist.dto.RegisterRequest;
import com.todolist.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/")
    public String indexRedirect() {
        return "redirect:/tasks";
    }


    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "Invalid username or password.");
        }
        if (logout != null) {
            model.addAttribute("logoutMsg", "You have been logged out.");
        }
        model.addAttribute("appName",      com.todolist.config.AppConfig.ConfigHolder.getInstance().getAppName());
        model.addAttribute("appVersion",   com.todolist.config.AppConfig.ConfigHolder.getInstance().getAppVersion());
        return "login";
    }


    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        model.addAttribute("appName",      com.todolist.config.AppConfig.ConfigHolder.getInstance().getAppName());
        model.addAttribute("appVersion",   com.todolist.config.AppConfig.ConfigHolder.getInstance().getAppVersion());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute RegisterRequest req, Model model) {
        try {
            userService.register(req.getUsername(), req.getEmail(), req.getPassword());
            return "redirect:/login?registered=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("registerRequest", req);
            return "register";
        }
    }
}
