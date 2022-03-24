package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.config.SecurityConfig;
import com.volunteer.Volunteer.Organization.models.Comments;
import com.volunteer.Volunteer.Organization.models.Posts;
import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.repository.CommentsRepository;
import com.volunteer.Volunteer.Organization.repository.PostsRepository;
import com.volunteer.Volunteer.Organization.service.UserService;
import org.apache.catalina.realm.GenericPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @GetMapping("/")
    public String main(Model model, HttpServletRequest request) {
        String role = UserService.getCurrentRole();
        if(role.equals("admin"))    {
            return "redirect:/admin/";
        }
        else if(role.equals("user")) {
                return "redirect:/user/";
            }
        return "main_page";
    }
}
