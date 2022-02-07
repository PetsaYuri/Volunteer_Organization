package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.service.ViewCandidatesService;
import com.volunteer.Volunteer.Organization.service.ViewUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

    @Autowired
    private ViewCandidatesService candidatesService;

    @Autowired
    private ViewUsersService usersService;

    private final String PATH_TO_TEMPLATE = "admin/";

    private final String FILE_PATH = "icon/uploads/candidates/";

    @GetMapping("/admin")
    public String admin(Model model) {
        return PATH_TO_TEMPLATE + "admin";
    }

    @GetMapping("/view_candidates")
    public String viewCandidates(Model model)  {
        model.addAttribute("filePath", FILE_PATH);
        model.addAttribute("forms", candidatesService.findAllCandidates());
        return PATH_TO_TEMPLATE + "view_candidates";
    }

    @PostMapping("/view_candidates")
    public String viewCandidates(@RequestParam String newStatus, @RequestParam long candidateId, @RequestParam String currentFilter, Model model)  {
        model.addAttribute("filePath", FILE_PATH);
        model.addAttribute("currentFilter", currentFilter);

        candidatesService.changeStatusForCandidate(candidateId, newStatus);
        //view the current filter and send forms to the client with the current filter
        model.addAttribute("forms", candidatesService.checkCurrentFilter(currentFilter));
        return PATH_TO_TEMPLATE + "view_candidates";
    }

    @PostMapping("/view_candidates_filter")
    public String viewPostFilter(@RequestParam String filter, Model model)  {
        model.addAttribute("filePath", FILE_PATH);
        model.addAttribute("currentFilter", filter);
        model.addAttribute("forms", candidatesService.findByFilter(filter));
        return PATH_TO_TEMPLATE + "view_candidates";
    }

    @GetMapping("/view_users")
    public String viewUsers(Model model)    {
        model.addAttribute("users", usersService.findAllUsers());
        return PATH_TO_TEMPLATE + "view_users";
    }

    public String getPATH_TO_TEMPLATE() {
        return PATH_TO_TEMPLATE;
    }

    public String getFILE_PATH() {
        return FILE_PATH;
    }
}
