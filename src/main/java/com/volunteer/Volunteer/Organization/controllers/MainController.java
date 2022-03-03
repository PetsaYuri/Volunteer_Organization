package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.config.SecurityConfig;
import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.service.UserService;
import org.apache.catalina.realm.GenericPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class MainController {

    @GetMapping("/")
    public static String main(Model model, HttpServletRequest request) {
        String role = UserService.getCurrentRole();
        System.out.println(role);
        if(role.equals("admin"))    {
            return "redirect:/admin";
        }
        else if(role.equals("user")) {
                return "redirect:/user";
            }
        return "main_page";
    }
}
