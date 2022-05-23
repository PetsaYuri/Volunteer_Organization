package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.models.SuggestedPosts;
import com.volunteer.Volunteer.Organization.models.Volunteers;
import com.volunteer.Volunteer.Organization.repository.CategoriesRepository;
import com.volunteer.Volunteer.Organization.repository.VolunteersRepository;
import com.volunteer.Volunteer.Organization.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.volunteer.Volunteer.Organization.service.UploadsPhotosService.PATH_TO_VOLUNTEERS_UPLOADS;

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

    @Autowired
    private EditorService editorService;

    @Autowired
    private MainService mainService;

    @Autowired
    private UploadsPhotosService uploadsPhotosService;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @GetMapping("/")
    public String userPage(Model model, HttpServletRequest request)    {
        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return PATH_TO_USER_FOLDER + "user_page";
    }

    @GetMapping("/view_volunteers")
    public String viewCandidates(Model model)  {
        model.addAttribute("filePath", PATH_TO_VOLUNTEERS_UPLOADS);
        model.addAttribute("candidates", volunteersRepository.findAll());
        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return PATH_TO_USER_FOLDER + "view_volunteers";
    }

    @PostMapping("/view_volunteers_filter")
    public String viewPostFilter(@RequestParam String filter, Model model)  {
        model.addAttribute("filePath", PATH_TO_VOLUNTEERS_UPLOADS);
        model.addAttribute("currentFilter", filter);
        model.addAttribute("volunteers", volunteersRepository.findByStatusOrderById(filter));
        return PATH_TO_USER_FOLDER + "view_volunteers";
    }

    @GetMapping("/suggest_post")
    public String suggestPost(Model model) {
        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        model.addAttribute("categories", categoriesRepository.findAll());
        return PATH_TO_USER_FOLDER + "suggest_post";
    }

    @PostMapping("/suggest_post")
    public String suggestPost(@RequestParam String title, @RequestParam String description,
                              @RequestParam Long idCategory, @RequestParam MultipartFile image,
                              Model model, HttpServletRequest request) throws IOException {
        String filename = uploadsPhotosService.createFilenameWithUUID(image.getOriginalFilename());
        SuggestedPosts suggestedPosts = editorService.addSuggestedPost(title, description, idCategory, request);
        uploadsPhotosService.saveFile(image, filename, suggestedPosts);
        return "redirect:/user/?SuggestedPosts";
    }

    @GetMapping("/csv_volunteers")
    public String csvReport(HttpServletResponse response) throws IOException {
        response = csvService.settingsOfResponse(response, "candidates");
        Iterable<Volunteers> volunteers = volunteersRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        csvService.createAndSendCsvFileVolunteers(response, volunteers);
        return "redirect:/view_volunteers";
    }
}
