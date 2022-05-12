package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.models.Volunteers;
import com.volunteer.Volunteer.Organization.repository.VolunteersRepository;
import com.volunteer.Volunteer.Organization.service.ExportCSVService;
import com.volunteer.Volunteer.Organization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.volunteer.Volunteer.Organization.service.UploadsPhotosService.PATH_TO_PHOTO;

@Controller
@RequestMapping("/user")
public class UserController {

    public static final String PATH_TO_USER_FOLDER = "user/";

    @Autowired
    private UserService userService;

    @Autowired
    private VolunteersRepository volunteersRepository;

    @Autowired
    private ExportCSVService csvService;

    @GetMapping("/")
    public String userPage(Model model, HttpServletRequest request)    {
        return PATH_TO_USER_FOLDER + "user_page";
    }

    @GetMapping("/view_volunteers")
    public String viewCandidates(Model model)  {
        model.addAttribute("filePath", PATH_TO_PHOTO);
        model.addAttribute("candidates", volunteersRepository.findAll());
        return PATH_TO_USER_FOLDER + "view_volunteers";
    }

    @PostMapping("/view_volunteers_filter")
    public String viewPostFilter(@RequestParam String filter, Model model)  {
        model.addAttribute("filePath", PATH_TO_PHOTO);
        model.addAttribute("currentFilter", filter);
        model.addAttribute("volunteers", volunteersRepository.findByStatusOrderById(filter));
        return PATH_TO_USER_FOLDER + "view_volunteers";
    }

    @GetMapping("/csv_volunteers")
    public String csvReport(HttpServletResponse response) throws IOException {
        response = csvService.settingsOfResponse(response, "candidates");
        Iterable<Volunteers> volunteers = volunteersRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        csvService.createAndSendCsvFileVolunteers(response, volunteers);
        return "redirect:/view_volunteers";
    }
}
