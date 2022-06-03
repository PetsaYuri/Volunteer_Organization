package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.exceptions.*;
import com.volunteer.Volunteer.Organization.models.ProjectInfo;
import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.models.Candidates;
import com.volunteer.Volunteer.Organization.repository.ProjectInfoRepository;
import com.volunteer.Volunteer.Organization.repository.RolesRepository;
import com.volunteer.Volunteer.Organization.repository.UsersRepository;
import com.volunteer.Volunteer.Organization.repository.CandidatesRepository;
import com.volunteer.Volunteer.Organization.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.UUID;

import static com.volunteer.Volunteer.Organization.service.UploadsPhotosService.PATH_TO_VOLUNTEERS_UPLOADS;

@Controller
@RequestMapping("/admin")
public class AdminController {

    public static final String PATH_TO_ADMIN_FOLDER = "admin/";

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private ExportCSVService csvService;

    @Autowired
    private CandidatesRepository candidatesRepository;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectInfoRepository projectInfoRepository;

    @Autowired
    private MainService mainService;

    @Autowired
    private UploadsPhotosService uploadsPhotosService;

    @Autowired
    private MailSenderService mailSenderService;

    @GetMapping("/")
    public String admin(Model model, HttpServletRequest request) {
        Users user = usersRepository.findByEmail(request.getRemoteUser());
        if (user != null) {
            model.addAttribute("role", user.getRoles().getRole());
        }

        ProjectInfo projectInfo = projectInfoRepository.getById(Long.valueOf(1));
        model.addAttribute("projectInfo", projectInfo);
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return PATH_TO_ADMIN_FOLDER + "admin_page";
    }

    @PostMapping("/edit_project_name")
    public String editProjectName(@RequestParam String projectName) {
        mainService.editProjectName(projectName);
        return "redirect:/admin/";
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
            model.addAttribute("pages", mainService.getPages(candidates.getTotalPages()));
            model.addAttribute("URI_page", "/admin/view_volunteers?page=");
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
            return PATH_TO_ADMIN_FOLDER + "view_candidates";
        }   catch (IncorrectQueryException ex)  {
            return "redirect:/admin/view_candidates?IncorrectQuery";
        }   catch (NotFoundByQueryException ex)  {
            return "redirect:/admin/view_candidates?NotFoundByQuery";
        }
    }

    @PostMapping("/view_candidates_filter")
    public String viewPostFilter(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                                 @RequestParam String filter, Model model)  {
        return "redirect:/admin/view_candidates?page=" + pageable.getPageNumber() + "&&size=" + pageable.getPageSize() + "&&filter=" + filter;
    }

    @PostMapping("/accept_user")
    public String acceptUser(@RequestParam long id)  {
        try {
            Users user = userService.acceptUser(id);
            String message = userService.sendAcceptanceMessage(id);
            mailSenderService.send(user.getEmail(), "Відповідь на заявку", message);
        }   catch (CandidateNotFoundException ex)   {
            return "redirect:/admin/view_candidates?CandidateNotFound";
        }
        return "redirect:/admin/view_candidates";
    }

    @PostMapping("/deny_user")
    public String denyUser(@RequestParam long id)   {
        Candidates candidate = userService.denyUser(id);
        String message = userService.sendDenialMessage(id);
        mailSenderService.send(candidate.getEmail(), "Відповідь на заявку", message);
        return "redirect:/admin/view_candidates";
    }

    @PostMapping("/previous_page_candidates")
    public String previousPageVolunteers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                               @RequestParam String filter, @RequestParam(defaultValue = "name") String field,
                               @RequestParam(required = false) String query)  {
        try {
            String URI = "redirect:/admin/view_candidates";
            String paramPageable = mainService.previousPage(pageable);
            URI += paramPageable + "&&filter=" + filter;
            if (query != null)  {
                URI += "&&field=" + field + "&&query=" + query;
            }
            return URI;
        }   catch (ItLastPageException ex)  {
            return "redirect:/admin/view_candidates?ItLastPage";
        }
    }

    @PostMapping("/next_page_candidates")
    public String nextPageVolunteers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                           @RequestParam String filter, @RequestParam(defaultValue = "name") String field,
                           @RequestParam(required = false) String query, @RequestParam int totalPage)  {
        try {
            String URI = "redirect:/admin/view_candidates";
            String paramPageable = mainService.nextPage(pageable, totalPage);
            URI += paramPageable + "&&filter=" + filter;
            if (query != null)  {
                URI += "&&field=" + field + "&&query=" + query;
            }
            return URI;
        }   catch (NotExistsNextPageException ex)  {
            return "redirect:/admin/view_candidates?NotExistsNextPage";
        }
    }

    @PostMapping("pageSize_candidates")
    public String pageSizeVolunteers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                           @RequestParam String currentFilter, @RequestParam(defaultValue = "name") String field,
                           @RequestParam(required = false) String query)    {
        String URI = "redirect:/admin/view_candidates?page=" + pageable.getPageNumber() + "" +
                "&&size=" + pageable.getPageSize() + "&&filter=" + currentFilter;
        if (query != null)  {
            URI += "&&field=" + field + "&&query=" + query;
        }
        return URI;
    }

    @PostMapping("/search_candidates")
    public String searchVolunteers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                         @RequestParam String filter, @RequestParam String query, @RequestParam String field)  {
        return "redirect:/admin/view_candidates?page=" + pageable.getPageNumber() + "&&size=" + pageable.getPageSize() +
                "&&filter=" + filter + "&&field=" + field + "&&query=" + query;
    }

    @GetMapping("/view_users")
    public String viewUsers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                            @RequestParam(defaultValue = "email") String field, @RequestParam(required = false) String query,
                            Model model)    {
        try {
            Page<Users> users;
            if (query == null) {
                users = usersRepository.findAll(pageable);
            }   else {
                users = userService.searchByField(field, query, pageable);
            }

            if (!users.hasContent())    {
                throw new NotFoundByQueryException();
            }
            model.addAttribute("users", users);

            //pagination
            model.addAttribute("pages", mainService.getPages(users.getTotalPages()));
            model.addAttribute("URI_page", "/admin/view_users?page=");
            model.addAttribute("URI_size", "&&size=" + pageable.getPageSize());
            model.addAttribute("URI_field", "&&field=" + field);
            if (query != null) {
                model.addAttribute("URI_query", "&&query=" + query);
            }

            //values necessary for work
            model.addAttribute("query", query);
            model.addAttribute("field", field);
            model.addAttribute("filePath", PATH_TO_VOLUNTEERS_UPLOADS);
            model.addAttribute("projectInfo", mainService.getProjectInfo());
            model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        }   catch (IncorrectQueryException ex)  {
            return "redirect:/admin/view_users?IncorrectQuery";
        }   catch (NotFoundByQueryException ex)  {
            return "redirect:/admin/view_users?NotFoundByQuery";
        }

        return PATH_TO_ADMIN_FOLDER + "view_users";
    }

    @PostMapping("/previous_page_users")
    public String previousPageUsers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                               @RequestParam(defaultValue = "email") String field, @RequestParam(required = false) String query)  {
        try {
            String URI = "redirect:/admin/view_users";
            String paramPageable = mainService.previousPage(pageable);
            URI += paramPageable;
            if (query != null)  {
                URI += "&&field=" + field + "&&query=" + query;
            }
            return URI;
        }   catch (ItLastPageException ex)  {
            return "redirect:/admin/view_users?ItLastPage";
        }
    }

    @PostMapping("/next_page_users")
    public String nextPageUsers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                                @RequestParam(defaultValue = "email") String field, @RequestParam(required = false) String query,
                                @RequestParam int totalPage)  {
        try {
            String URI = "redirect:/admin/view_users";
            String paramPageable = mainService.nextPage(pageable, totalPage);
            URI += paramPageable;
            if (query != null)  {
                URI += "&&field=" + field + "&&query=" + query;
            }
            return URI;
        }   catch (NotExistsNextPageException ex)  {
            return "redirect:/admin/view_users?NotExistsNextPage";
        }
    }

    @PostMapping("pageSize_users")
    public String pageSizeUsers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                                @RequestParam(defaultValue = "email") String field, @RequestParam(required = false) String query)    {
        String URI = "redirect:/admin/view_users?page=" + pageable.getPageNumber() + "" + "&&size=" + pageable.getPageSize();
        if (query != null)  {
            URI += "&&field=" + field + "&&query=" + query;
        }
        return URI;
    }

    @PostMapping("/search_users")
    public String searchUsers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                              @RequestParam(defaultValue = "email") String field, @RequestParam(required = false) String query)  {
        return "redirect:/admin/view_users?page=" + pageable.getPageNumber() + "&&size=" + pageable.getPageSize() +
                 "&&field=" + field + "&&query=" + query;
    }

    @PostMapping("block_user")
    public String blockUser(@RequestParam long id)  {
        userService.blockUser(id);
        return "redirect:/admin/view_users";
    }

    @PostMapping("unblock_user")
    public String unblockUser(@RequestParam long id)  {
        userService.unblockUser(id);
        return "redirect:/admin/view_users";
    }

    @GetMapping("/add_user")
    public String addUser(Model model)  {
        model.addAttribute("roles", rolesRepository.findAll());
        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return PATH_TO_ADMIN_FOLDER + "add_user";
    }

    @PostMapping("/add_user")
    public String postAddUser(@RequestParam String password, @RequestParam String selectedRole,
                              @RequestParam String repeatedPassword, @RequestParam String email,
                              @RequestParam(required = false) String name)    {
        try {
            if(userService.isAlreadyExistsEmail(email))   {
                throw new UserAlreadyExistsException();
            }

            if(!password.equals(repeatedPassword))    {
                throw new RepeatedPasswordIsInvalidException();
            }

            Candidates candidate = candidatesRepository.findByEmail(email);
            if (candidate == null)  {
                userService.addUser(password, selectedRole, email, name);
            }   else {
                userService.addUser(password, selectedRole, candidate);
            }
        }   catch (UserAlreadyExistsException ex)   {
            return "redirect:add_user?UserAlreadyExistsException";
        }   catch (RepeatedPasswordIsInvalidException ex)   {
            return "redirect:add_user?RepeatedPasswordIsInvalidException";
        }
        return "redirect:add_user?UserAdded";
    }

    @GetMapping("/csv_users")
    public String csvReport(HttpServletResponse response) throws IOException {
        response = csvService.settingsOfResponse(response, "users");
        Iterable<Users> users = usersRepository.findAll();
        csvService.createAndSendCsvFileUsers(response, users);
        return "redirect:/view_users";
    }

    @GetMapping("/upload_logo")
    public String uploadLogo(Model model)  {
        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return PATH_TO_ADMIN_FOLDER + "upload_logo";
    }

    @PostMapping("upload_logo")
    public String ulpoadLogo(@RequestParam(required = false) MultipartFile logo, HttpServletRequest request,
                             @RequestParam(required = false) MultipartFile imageNavbar, Model model) throws IOException {
        try {
            ProjectInfo projectInfo = projectInfoRepository.getById(Long.valueOf(1));
            if (!logo.isEmpty()) {
                String filename = uploadsPhotosService.createFilenameWithUUID(logo.getOriginalFilename());
                if (uploadsPhotosService.isIcoFile(filename)) {
                    uploadsPhotosService.saveFile(logo, filename, projectInfo);
                } else {
                    throw new NotAllowedFileFormatException();
                }
            }
            if (!imageNavbar.isEmpty()) {
                String filename = uploadsPhotosService.createFilenameWithUUID(imageNavbar.getOriginalFilename());
                uploadsPhotosService.saveFile(imageNavbar, filename, projectInfo);
            }
            return "redirect:/admin/?ImageDownload";
        }   catch (NotAllowedFileFormatException ex)    {
            return "redirect:/admin/upload_logo?NotAllowedFileFormatException";
        }
    }

    @PostMapping("edit_contacts")
    public String saveContacts(@RequestParam String telegram, @RequestParam String email,
                               @RequestParam String phone)    {
        mainService.editContacts(telegram, email, phone);
        return "redirect:/admin/";
    }
}