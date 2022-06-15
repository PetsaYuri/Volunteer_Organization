package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.exceptions.ItLastPageException;
import com.volunteer.Volunteer.Organization.exceptions.NotExistsNextPageException;
import com.volunteer.Volunteer.Organization.exceptions.NotFoundByQueryException;
import com.volunteer.Volunteer.Organization.exceptions.UserNotFoundException;
import com.volunteer.Volunteer.Organization.models.Categories;
import com.volunteer.Volunteer.Organization.models.Posts;
import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.repository.CategoriesRepository;
import com.volunteer.Volunteer.Organization.repository.PostsRepository;
import com.volunteer.Volunteer.Organization.repository.UsersRepository;
import com.volunteer.Volunteer.Organization.service.EditorService;
import com.volunteer.Volunteer.Organization.service.MailSenderService;
import com.volunteer.Volunteer.Organization.service.MainService;
import com.volunteer.Volunteer.Organization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import static com.volunteer.Volunteer.Organization.service.UploadsPhotosService.PATH_TO_POSTS_UPLOADS;
import static com.volunteer.Volunteer.Organization.service.UploadsPhotosService.PATH_TO_PROJECT_INFO_UPLOADS;

@Controller
public class MainController {

    @Autowired
    private EditorService editorService;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MainService mainService;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String main(Model model, HttpServletRequest request) {
        Users user = usersRepository.findByEmail(request.getRemoteUser());
        if (user != null) {
            model.addAttribute("role", user.getRoles().getRole());
        }

        Page<Posts> lastPosts = postsRepository.findAll(PageRequest.of(0, 3, Sort.Direction.DESC, "id"));
        model.addAttribute("posts", lastPosts);
        model.addAttribute("URI", "/blog/");
        model.addAttribute("filePathPosts", PATH_TO_POSTS_UPLOADS);

        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("filePath", PATH_TO_PROJECT_INFO_UPLOADS);
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return "main_page";
    }

    @GetMapping("/login")
    public String login(Model model)   {
        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return "login";
    }

    @GetMapping("/blog")
    public String blog(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 6) Pageable pageable,
                        @RequestParam(required = false) String query, Model model, HttpServletRequest request,
                       @RequestParam(required = false) Long idCategory)    {
        try {
            Page<Posts> posts;
            Categories category;

                if (idCategory == null) {
                category = editorService.firstCategory();
                if (query == null) {
                    posts = postsRepository.findAll(pageable);
                } else {
                    posts = postsRepository.findByTitleContainingIgnoreCase(query, pageable);
                    if (!posts.hasContent()) {
                        throw new NotFoundByQueryException();
                    }
                }
            }   else
                    if (idCategory == 0)    {
                        posts = postsRepository.findByDescriptionContainingIgnoreCase(query, pageable);
                        category = editorService.firstCategory();
                    }   else {
                category = categoriesRepository.getById(idCategory);
                if (query == null) {
                    posts = postsRepository.findByCategory(category, pageable);
                }   else {
                    posts = postsRepository.findByCategoryAndTitleContainingIgnoreCase(category, query, pageable);
                }
            }

            model.addAttribute("posts", posts);
            model.addAttribute("currentCategory", category);

            //pagination
            model.addAttribute("pages", mainService.getPages(posts.getTotalPages()));
            model.addAttribute("URI_page", "/blog?page=");
            model.addAttribute("URI_size", "&&size=" + pageable.getPageSize());
            if (query != null) {
                model.addAttribute("URI_query", "&&query=" + query);
            }

            //values necessary for work
            model.addAttribute("query", query);
            model.addAttribute("filePath", PATH_TO_POSTS_UPLOADS);
            model.addAttribute("URI", "blog/");
            model.addAttribute("pathEdit", "/editor/edit_post/");
            model.addAttribute("projectInfo", mainService.getProjectInfo());
            model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
            model.addAttribute("categories", categoriesRepository.findAll(Sort.by("id")));

            Users user = usersRepository.findByEmail(request.getRemoteUser());
            if (user != null) {
                String role = user.getRole();
                if (role.equals("admin"))   {
                    role = "editor";
                }
                model.addAttribute("role", role);
            }
            return "blog";
        }   catch (NotFoundByQueryException ex)  {
            return "redirect:/blog?NotFoundByQuery";
        }
    }

    @GetMapping("/blog/{idPost}")
    public String fullPost(Model model, @PathVariable Long idPost, HttpServletRequest request,
                           @PageableDefault(size = 3, direction = Sort.Direction.DESC, sort = "id") Pageable pageable)    {
        try {
            Posts post = postsRepository.getById(idPost);
            model.addAttribute("post", post);

            Page<Posts> lastPosts = editorService.lastPosts(post.getCategory(), pageable);
            model.addAttribute("lastPosts", lastPosts);
            model.addAttribute("filePath", PATH_TO_POSTS_UPLOADS);
            model.addAttribute("projectInfo", mainService.getProjectInfo());
            model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
            model.addAttribute("description", mainService.splitDescriptionOnParagraph(post.getDescription()));
            Users user = usersRepository.findByEmail(request.getRemoteUser());
            if (user != null) {
                String role = user.getRole();
                if (role.equals("admin"))   {
                    role = "editor";
                }
                model.addAttribute("role", role);
            }
            return "full_post";
        }   catch (Exception ex)    {
            return "";
        }
    }

    @PostMapping("/add_comment")
    public String addComment(Model model, @RequestParam String comment,
                             @RequestParam long id_post, HttpServletRequest request)  {
        try {

            editorService.addComment(comment, id_post, request);
            return "redirect:blog/" + id_post + "?AddedComment";
        }   catch (UserNotFoundException ex)   {
            return "redirect:blog/" + id_post + "?VolunteerNotFound";
        }

    }

    @PostMapping("delete_comment")
    public String deleteComment(@RequestParam long id, @RequestParam long idPost)  {
        editorService.deleteComment(id);
        return "redirect:/blog/" + idPost + "?DeletedComment";
    }

    @PostMapping("/allow_editing_comment")
    public String allowEditing(Model model, @RequestParam long idPost, @RequestParam long idComment,
                               HttpServletRequest request, @PageableDefault(size = 3, direction = Sort.Direction.DESC, sort = "id") Pageable pageable)    {
        model.addAttribute("edit", true);
        model.addAttribute("currentComment", idComment);
        return fullPost(model, idPost, request, pageable);
    }

    @PostMapping("/edit_comment")
    public String editComment(@RequestParam long idComment, @RequestParam String comment, @RequestParam long idPost) {
        editorService.editComment(idComment, comment);
        return "redirect:/blog/" + idPost + "?CommentEdited";
    }

    @PostMapping("/previous_page")
    public String previousPage(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 6) Pageable pageable,
                               @RequestParam(required = false) String query)  {
        try {
            String URI = "redirect:/blog";
            String paramPageable = mainService.previousPage(pageable);
            URI += paramPageable;
            if (query != null)  {
                URI += "&&query=" + query;
            }
            return URI;
        }   catch (ItLastPageException ex)  {
            return "redirect:/blog?ItLastPage";
        }
    }

    @PostMapping("/next_page")
    public String nextPage(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 6) Pageable pageable,
                           @RequestParam(required = false) String query, @RequestParam int totalPage)  {
        try {
            String URI = "redirect:/blog";
            String paramPageable = mainService.nextPage(pageable, totalPage);
            URI += paramPageable;
            if (query != null)  {
                URI += "&&query=" + query;
            }
            return URI;
        }   catch (NotExistsNextPageException ex)  {
            return "redirect:/blog?NotExistsNextPage";
        }
    }

    @PostMapping("pageSize")
    public String pageSize(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 6) Pageable pageable,
                           @RequestParam(required = false) String query)    {
        String URI = "redirect:/blog?page=" + pageable.getPageNumber() + "" +
                "&&size=" + pageable.getPageSize();
        if (query != null)  {
            URI += "&&query=" + query;
        }
        return URI;
    }

    @PostMapping("/posts_search")
    public String search(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 6) Pageable pageable,
                         @RequestParam(required = false) String query, @RequestParam long idCategory,
                         Model model, HttpServletRequest request)  {
        return blog(pageable, query, model, request, idCategory);
    }

    @GetMapping("/verification")
    public String verification(Model model)    {
        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return "verification";
    }

    @PostMapping("/verification")
    public String verification(@RequestParam String email)    {
        try {
            Users user = userService.verification(email);
            String message = userService.sendRecoveryMessage(user);
            mailSenderService.send(email, "Відновлення доступу", message);
            return "redirect:/?SendingMessageToEmail";
        }   catch (NullPointerException ex) {
            return "redirect:/verification?UserNotFound";
        }
    }
}
