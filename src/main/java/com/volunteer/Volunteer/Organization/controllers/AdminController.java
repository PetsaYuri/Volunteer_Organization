package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.exceptions.*;
import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.models.Volunteers;
import com.volunteer.Volunteer.Organization.repository.RolesRepository;
import com.volunteer.Volunteer.Organization.repository.UsersRepository;
import com.volunteer.Volunteer.Organization.repository.VolunteersRepository;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static com.volunteer.Volunteer.Organization.service.UploadsPhotosService.PATH_TO_PHOTO;

@Controller
@RequestMapping("/admin")
public class AdminController {

    public static final String PATH_TO_ADMIN_FOLDER = "admin/";

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private ExportCSVService csvService;

    @Autowired
    private VolunteersRepository volunteersRepository;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MainService mainService;

    @GetMapping("/")
    public String admin(Model model) {
        return PATH_TO_ADMIN_FOLDER + "admin_page";
    }

    @GetMapping("/view_volunteers")
    public String viewVolunteers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                                  @RequestParam(defaultValue = "waiting") String filter, @RequestParam(defaultValue = "name") String field,
                                 @RequestParam(required = false) String query, Model model)    {
        try {
            Page<Volunteers> volunteers;
            if (query == null) {
                volunteers = volunteersRepository.findByStatus(filter, pageable);
            } else {
                volunteers = volunteerService.searchByField(field, query, pageable);
            }

            if (!volunteers.hasContent()) {
                throw new NotFoundByQueryException();
            }
            model.addAttribute("volunteers", volunteers);

            //pagination
            model.addAttribute("pages", mainService.getPages(volunteers.getTotalPages()));
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
            model.addAttribute("filePath", PATH_TO_PHOTO);
            return PATH_TO_ADMIN_FOLDER + "view_volunteers";
        }   catch (IncorrectQueryException ex)  {
            return "redirect:/admin/view_volunteers?IncorrectQuery";
        }   catch (NotFoundByQueryException ex)  {
            return "redirect:/admin/view_volunteers?NotFoundByQuery";
        }
    }

    @PostMapping("/view_volunteers_filter")
    public String viewPostFilter(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                                 @RequestParam String filter, Model model)  {
        return "redirect:/admin/view_volunteers?page=" + pageable.getPageNumber() + "&&size=" + pageable.getPageSize() + "&&filter=" + filter;
    }

    @PostMapping("/accept_user")
    public String acceptUser(@RequestParam long id)  {
        try {
            Volunteers volunteer = volunteersRepository.findById(id);
            if (volunteer != null) {
                volunteer.setStatus("accept");
                volunteersRepository.save(volunteer);

                String password = UUID.randomUUID().toString();
                userService.addUser(password, "user", volunteer);
            } else {
                throw new VolunteerNotFoundException();
            }
        }   catch (VolunteerNotFoundException ex)   {
            return "redirect:/admin/view_volunteers?VolunteerNotFound";
        }
        return "redirect:/admin/view_volunteers";
    }

    @PostMapping("/deny_user")
    public String denyUser(@RequestParam long id)   {
        //write and send message about deny by volunteers
        Volunteers volunteer = volunteersRepository.findById(id);
        volunteer.setStatus("deny");
        volunteersRepository.save(volunteer);
        return "redirect:/admin/view_volunteers";
    }

    @PostMapping("/previous_page_volunteers")
    public String previousPageVolunteers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                               @RequestParam String filter, @RequestParam(defaultValue = "name") String field,
                               @RequestParam(required = false) String query)  {
        try {
            String URI = "redirect:/admin/view_volunteers";
            String paramPageable = mainService.previousPage(pageable);
            URI += paramPageable + "&&filter=" + filter;
            if (query != null)  {
                URI += "&&field=" + field + "&&query=" + query;
            }
            return URI;
        }   catch (ItLastPageException ex)  {
            return "redirect:/admin/view_volunteers?ItLastPage";
        }
    }

    @PostMapping("/next_page_volunteers")
    public String nextPageVolunteers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                           @RequestParam String filter, @RequestParam(defaultValue = "name") String field,
                           @RequestParam(required = false) String query, @RequestParam int totalPage)  {
        try {
            String URI = "redirect:/admin/view_volunteers";
            String paramPageable = mainService.nextPage(pageable, totalPage);
            URI += paramPageable + "&&filter=" + filter;
            if (query != null)  {
                URI += "&&field=" + field + "&&query=" + query;
            }
            return URI;
        }   catch (NotExistsNextPageException ex)  {
            return "redirect:/admin/view_volunteers?NotExistsNextPage";
        }
    }

    @PostMapping("pageSize_volunteers")
    public String pageSizeVolunteers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                           @RequestParam String currentFilter, @RequestParam(defaultValue = "name") String field,
                           @RequestParam(required = false) String query)    {
        String URI = "redirect:/admin/view_volunteers?page=" + pageable.getPageNumber() + "" +
                "&&size=" + pageable.getPageSize() + "&&filter=" + currentFilter;
        if (query != null)  {
            URI += "&&field=" + field + "&&query=" + query;
        }
        return URI;
    }

    @PostMapping("/search_volunteers")
    public String searchVolunteers(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                         @RequestParam String filter, @RequestParam String query, @RequestParam String field)  {
        return "redirect:/admin/view_volunteers?page=" + pageable.getPageNumber() + "&&size=" + pageable.getPageSize() +
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
            model.addAttribute("filePath", PATH_TO_PHOTO);
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
        return PATH_TO_ADMIN_FOLDER + "add_user";
    }

    @PostMapping("/add_user")
    public String postAddUser(@RequestParam String password, @RequestParam String selectedRole,
                              @RequestParam String repeatedPassword, @RequestParam String email,
                              @RequestParam(required = false) String name, Model model)    {
        try {
            if(userService.isAlreadyExistsEmail(email))   {
                throw new UserAlreadyExistsException();
            }

            if(!password.equals(repeatedPassword))    {
                throw new RepeatedPasswordIsInvalidException();
            }

            Volunteers volunteer = volunteersRepository.findByEmail(email);
            if (volunteer == null)  {
                userService.addUser(password, selectedRole, email, name);
            }   else {
                userService.addUser(password, selectedRole, volunteer);
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
}
