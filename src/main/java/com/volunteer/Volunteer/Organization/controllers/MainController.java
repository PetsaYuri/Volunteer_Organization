package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.config.SecurityConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public static String main(Model model) {
        String role = SecurityConfig.getRole();
        if(role.equals("ADMIN"))    {
            return "redirect:/admin";
        }
        else
            if(role.equals("USER")) {
                return "redirect:/user";
            }
            model.addAttribute("active", "main");

        return "main";
    }

    @GetMapping("/user")
    public String user(Model model) {
        return "user";
    }

}
