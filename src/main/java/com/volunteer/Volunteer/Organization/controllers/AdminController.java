package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.exceptions.RepeatedPasswordIsInvalidException;
import com.volunteer.Volunteer.Organization.exceptions.UserAlreadyExistsException;
import com.volunteer.Volunteer.Organization.service.CandidatesService;
import com.volunteer.Volunteer.Organization.service.RolesService;
import com.volunteer.Volunteer.Organization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.volunteer.Volunteer.Organization.service.UserService.*;

@Controller
public class AdminController {

    @Autowired
    private CandidatesService candidatesService;

    @Autowired
    private UserService userService;

    @Autowired
    private RolesService rolesService;

    @GetMapping("/admin")
    public String admin(Model model) {
        return PATH_TO_ADMIN_FOLDER + "admin_page";
    }

    @GetMapping("/admin/view_candidates")
    public String viewCandidates(Model model)  {
        model.addAttribute("currentRole", UserService.getCurrentRole());
        model.addAttribute("filePath", PATH_TO_PHOTO);
        model.addAttribute("forms", candidatesService.findAllCandidates());
        model.addAttribute("currentFilter", "ОЧІКУВАННЯ");
        model.addAttribute("header", PATH_TO_ADMIN_HEADER);
        model.addAttribute("blocks", PATH_TO_BLOCKS);
        return PATH_TO_ADMIN_FOLDER + "view_candidates";
    }

    @PostMapping("/admin/view_candidates")
    public String viewCandidates(@RequestParam String newStatus, @RequestParam long candidateId,
                                 @RequestParam String currentFilter, Model model)  {
        model.addAttribute("filePath", PATH_TO_PHOTO);
        model.addAttribute("currentFilter", currentFilter);

        candidatesService.changeStatusForCandidate(candidateId, newStatus);
        //view the current filter and send forms to the client with the current filter
        model.addAttribute("forms", candidatesService.checkCurrentFilter(currentFilter));
        model.addAttribute("currentRole", UserService.getCurrentRole());
        model.addAttribute("header", PATH_TO_ADMIN_HEADER);
        model.addAttribute("blocks", PATH_TO_BLOCKS);
        return PATH_TO_ADMIN_FOLDER + "view_candidates";
    }

    @PostMapping("/admin/view_candidates_filter")
    public String viewPostFilter(@RequestParam String filter, Model model)  {
        model.addAttribute("filePath", PATH_TO_PHOTO);
        model.addAttribute("currentFilter", filter);
        model.addAttribute("forms", candidatesService.findByFilter(filter));
        model.addAttribute("currentRole", UserService.getCurrentRole());
        model.addAttribute("header", PATH_TO_ADMIN_HEADER);
        model.addAttribute("blocks", PATH_TO_BLOCKS);
        return PATH_TO_ADMIN_FOLDER + "view_candidates";
    }

    @GetMapping("/admin/view_users")
    public String viewUsers(Model model)    {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("header", PATH_TO_ADMIN_HEADER);
        model.addAttribute("blocks", PATH_TO_BLOCKS);
        return PATH_TO_ADMIN_FOLDER + "view_users";
    }

    @GetMapping("/admin/add_user")
    public String addUser(Model model)  {
        model.addAttribute("roles", rolesService.getAllRoles());
        model.addAttribute("header", PATH_TO_ADMIN_HEADER);
        model.addAttribute("blocks", PATH_TO_BLOCKS);
        return PATH_TO_ADMIN_FOLDER + "add_user";
    }

    @PostMapping("/admin/add_user")
    public String postAddUser(@RequestParam String username, @RequestParam String password,
                             @RequestParam String selectedRole, @RequestParam String repeatedPassword,
                              Model model)    {
        try {
            if(!password.equals(repeatedPassword))    {
                throw new RepeatedPasswordIsInvalidException();
            }

            userService.addUser(username, password, selectedRole);
        }   catch (UserAlreadyExistsException ex)   {
            return "redirect:add_user?UserAlreadyExistsException";
        }   catch (RepeatedPasswordIsInvalidException ex)   {
            return "redirect:add_user?RepeatedPasswordIsInvalidException";
        }
        model.addAttribute("header", PATH_TO_ADMIN_HEADER);
        model.addAttribute("blocks", PATH_TO_BLOCKS);
        return "redirect:add_user?UserAdded";
    }
}
