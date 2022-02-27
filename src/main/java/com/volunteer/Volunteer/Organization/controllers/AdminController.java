package com.volunteer.Volunteer.Organization.controllers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.volunteer.Volunteer.Organization.exceptions.UserAlreadyExistsException;
import com.volunteer.Volunteer.Organization.models.Roles;
import com.volunteer.Volunteer.Organization.service.RolesService;
import com.volunteer.Volunteer.Organization.service.UserService;
import com.volunteer.Volunteer.Organization.service.CandidatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

    @Autowired
    private CandidatesService candidatesService;

    @Autowired
    private UserService usersService;

    @Autowired
    private RolesService rolesService;

    private final String PATH_TO_TEMPLATE = "admin/";

    private final String URI_PATH = "/admin";

    private final String FILE_PATH = "/icon/uploads/candidates/";

    @GetMapping("/admin")
    public String admin(Model model) {
        return PATH_TO_TEMPLATE + "admin";
    }

    @GetMapping("/admin/view_candidates")
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

    @GetMapping("/add_user")
    public String addUser(Model model)  {
        model.addAttribute("roles", rolesService.getAllRoles());
        return PATH_TO_TEMPLATE + "add_user";
    }

    @PostMapping("/add_user")
    public String postAddUser(@RequestParam String username, @RequestParam String password,
                             @RequestParam String selectedRole,  Model model)    {
        try {
            usersService.addUser(username, password, selectedRole);
        }   catch (UserAlreadyExistsException ex)   {
            return "redirect:add_user?UserAlreadyExistsException";
        }
        return "redirect:add_user?UserAdded";
    }

   /* @GetMapping("/add_roles")
    public String addRoles(Model model)    {
        return PATH_TO_TEMPLATE + "add_roles";
    }

    @PostMapping("/add_roles")
    public String postAddRoles(@RequestParam String role, @RequestParam String description, Model model) {
        rolesService.addNewRole(role, description);

        return "redirect:add_roles?RoleAdded";
    }*/

    public String getPATH_TO_TEMPLATE() {
        return PATH_TO_TEMPLATE;
    }

    public String getFILE_PATH() {
        return FILE_PATH;
    }
}
