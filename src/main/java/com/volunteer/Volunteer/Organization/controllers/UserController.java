package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.models.Candidates;
import com.volunteer.Volunteer.Organization.service.CandidatesService;
import com.volunteer.Volunteer.Organization.service.ExportCSVService;
import com.volunteer.Volunteer.Organization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.volunteer.Volunteer.Organization.service.UserService.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CandidatesService candidatesService;

    @Autowired
    private ExportCSVService csvService;

    @GetMapping("/")
    public String userPage(Model model)    {
        return PATH_TO_USER_FOLDER + "user_page";
    }

    @GetMapping("/view_candidates")
    public String viewCandidates(Model model)  {
        String currentRole = "/" + UserService.getCurrentRole();
        model.addAttribute("currentRole", currentRole);

        model.addAttribute("filePath", PATH_TO_PHOTO);
        model.addAttribute("candidates", candidatesService.findAllCandidates());
        model.addAttribute("header", PATH_TO_USER_HEADER);
        model.addAttribute("blocks", PATH_TO_BLOCKS);
        return PATH_TO_ADMIN_FOLDER + "view_candidates";
    }

    @PostMapping("/view_candidates_filter")
    public String viewPostFilter(@RequestParam String filter, Model model)  {
        model.addAttribute("filePath", PATH_TO_PHOTO);
        model.addAttribute("currentFilter", filter);
        model.addAttribute("candidates", candidatesService.findByFilter(filter));
        model.addAttribute("header", PATH_TO_USER_HEADER);
        model.addAttribute("blocks", PATH_TO_BLOCKS);
        return PATH_TO_ADMIN_FOLDER + "view_candidates";
    }

    @GetMapping("/view_users")
    public String viewUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("header", PATH_TO_USER_HEADER);
        model.addAttribute("blocks", PATH_TO_BLOCKS);
        return PATH_TO_ADMIN_FOLDER + "view_users";
    }

    @GetMapping("/csv_candidates")
    public String csvReport(HttpServletResponse response) throws IOException {
        response = csvService.settingsOfResponse(response, "candidates");
        Iterable<Candidates> candidates = candidatesService.findAllCandidates();
        csvService.createAndSendCsvFileCandidates(response, candidates);
        return "redirect:/view_candidates";
    }
}
