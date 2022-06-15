package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.exceptions.*;
import com.volunteer.Volunteer.Organization.models.Posts;
import com.volunteer.Volunteer.Organization.models.Roles;
import com.volunteer.Volunteer.Organization.models.SuggestedPosts;
import com.volunteer.Volunteer.Organization.models.Candidates;
import com.volunteer.Volunteer.Organization.repository.CategoriesRepository;
import com.volunteer.Volunteer.Organization.repository.CandidatesRepository;
import com.volunteer.Volunteer.Organization.repository.PostsRepository;
import com.volunteer.Volunteer.Organization.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

import static com.volunteer.Volunteer.Organization.service.UploadsPhotosService.PATH_TO_POSTS_UPLOADS;
import static com.volunteer.Volunteer.Organization.service.UploadsPhotosService.PATH_TO_VOLUNTEERS_UPLOADS;

@Controller
@RequestMapping("/user")
public class UserController {

    public static final String PATH_TO_USER_FOLDER = "user/";

    @Autowired
    private UserService userService;

    @Autowired
    private CandidatesRepository candidatesRepository;

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

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private PostsRepository postsRepository;

    @GetMapping("/")
    public String userPage(Model model, HttpServletRequest request)    {
        Page<Posts> lastPosts = postsRepository.findAll(PageRequest.of(0, 3, Sort.Direction.DESC, "id"));
        model.addAttribute("posts", lastPosts);
        model.addAttribute("URI", "/blog/");
        model.addAttribute("filePathPosts", PATH_TO_POSTS_UPLOADS);

        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return PATH_TO_USER_FOLDER + "user_page";
    }

    @GetMapping("/view_candidates")
    public String viewVolunteers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                                 @RequestParam(defaultValue = "waiting") String filter, @RequestParam(defaultValue = "name") String field,
                                 @RequestParam(required = false) String query, Model model)    {
        try {
            Page<Candidates> candidates;
            if (query == null) {
                candidates = candidatesRepository.findByStatus(filter, pageable);
            } else {
                candidates = candidateService.searchByField(field, query, filter, pageable);
                if (!candidates.hasContent()) {
                    throw new NotFoundByQueryException();
                }
            }
            model.addAttribute("candidates", candidates);

            //pagination
            model.addAttribute("pageable", pageable);
            model.addAttribute("pages", mainService.getPages(candidates.getTotalPages()));
            model.addAttribute("URI_page", "/user/view_candidates?page=");
            model.addAttribute("URI_size", "&&size=" + pageable.getPageSize());
            model.addAttribute("URI_filter", "&&filter=" + filter);
            model.addAttribute("URI_field", "&&field=" + field);
            if (query != null) {
                model.addAttribute("URI_query", "&&query=" + query);
            }

            //values necessary for work
            model.addAttribute("currentFilter", filter);
            model.addAttribute("query", query);
            model.addAttribute("field", field);
            model.addAttribute("filePath", PATH_TO_VOLUNTEERS_UPLOADS);
            model.addAttribute("projectInfo", mainService.getProjectInfo());
            model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
            return PATH_TO_USER_FOLDER + "view_candidates";
        }   catch (IncorrectQueryException ex)  {
            return "redirect:/user/view_candidates?IncorrectQuery";
        }   catch (NotFoundByQueryException ex)  {
            return "redirect:/user/view_candidates?NotFoundByQuery";
        }
    }

    @PostMapping("/view_candidates_filter")
    public String viewPostFilter(@RequestParam String filter, Model model)  {
        model.addAttribute("filePath", PATH_TO_VOLUNTEERS_UPLOADS);
        model.addAttribute("currentFilter", filter);
        model.addAttribute("candidates", candidatesRepository.findByStatusOrderById(filter));
        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return PATH_TO_USER_FOLDER + "view_candidates";
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

    @GetMapping("/csv_candidates")
    public String csvReport(HttpServletResponse response) throws IOException {
        response = csvService.settingsOfResponse(response, "candidates");
        Iterable<Candidates> volunteers = candidatesRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        csvService.createAndSendCsvFileVolunteers(response, volunteers);
        return "redirect:/view_candidates";
    }

    @GetMapping("/change_password")
    public String changePassword(Model model)  {
        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return PATH_TO_USER_FOLDER + "change_password";
    }

    @PostMapping("/change_password")
    public String changePassword(@RequestParam String curPassword, @RequestParam String password,
                                 @RequestParam String rePassword, HttpServletRequest request)   {
        try {
            if (password.equals(rePassword)) {
                Roles roles = userService.changePassword(request, curPassword, password);
                return "redirect:/" + roles.getRole() + "/";
            }   else {
                throw new RepeatedPasswordIsInvalidException();
            }
        }  catch (UserNotFoundException ex) {
            return "redirect:/user/change_password?UserNotFound";
        }   catch (RepeatedPasswordIsInvalidException ex)   {
            return "redirect:/user/change_password?RepeatedPasswordIsInvalid";
        }   catch (CurrentPassIsInvalid ex) {
            return "redirect:/user/change_password?CurrentPassIsInvalid";
        }
    }
}
