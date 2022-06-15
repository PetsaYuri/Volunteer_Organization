package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.exceptions.NotFoundByQueryException;
import com.volunteer.Volunteer.Organization.exceptions.PostNotFoundException;
import com.volunteer.Volunteer.Organization.exceptions.UserNotFoundException;
import com.volunteer.Volunteer.Organization.models.Categories;
import com.volunteer.Volunteer.Organization.models.Posts;
import com.volunteer.Volunteer.Organization.models.SuggestedPosts;
import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.repository.CategoriesRepository;
import com.volunteer.Volunteer.Organization.repository.PostsRepository;
import com.volunteer.Volunteer.Organization.repository.SuggestedPostsRepository;
import com.volunteer.Volunteer.Organization.repository.UsersRepository;
import com.volunteer.Volunteer.Organization.service.EditorService;
import com.volunteer.Volunteer.Organization.service.MainService;
import com.volunteer.Volunteer.Organization.service.UploadsPhotosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.volunteer.Volunteer.Organization.service.EditorService.PATH_TO_EDITOR_FOLDER;
import static com.volunteer.Volunteer.Organization.service.UploadsPhotosService.PATH_TO_POSTS_UPLOADS;
import static com.volunteer.Volunteer.Organization.service.UploadsPhotosService.PATH_TO_SUGGESTED_POSTS_UPLOADS;

@Controller
@RequestMapping("/editor")
public class EditorContoller {

    @Autowired
    private EditorService editorService;

    @Autowired
    private UploadsPhotosService uploadsPhotosService;

    @Autowired
    private MainService mainService;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private SuggestedPostsRepository suggestedPostsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/")
    public String editorPage(Model model)  {
        Page<Posts> lastPosts = postsRepository.findAll(PageRequest.of(0, 3, Sort.Direction.DESC, "id"));
        model.addAttribute("posts", lastPosts);
        model.addAttribute("URI", "/blog/");
        model.addAttribute("filePathPosts", PATH_TO_POSTS_UPLOADS);

        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return PATH_TO_EDITOR_FOLDER + "editor_page";
    }

    @GetMapping("/add_post")
    public String addPost(Model model) {
        model.addAttribute("categories", categoriesRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return PATH_TO_EDITOR_FOLDER + "add_post";
    }

    @PostMapping("/add_post")
    public String addNewPost(Model model, @RequestParam String title, @RequestParam String description,
                             @RequestParam MultipartFile image, @RequestParam long idCategory,
                             HttpServletRequest request) throws IOException {
        try {
            String filename = uploadsPhotosService.createFilenameWithUUID(image.getOriginalFilename());
            Posts post = editorService.addNewPost(title, description, request, idCategory);
            uploadsPhotosService.saveFile(image, filename, post);
            return "redirect:/blog?AddedPost";
        }   catch (UserNotFoundException ex)    {
            return "redirect:add_post?UserNotFound";
        }

    }

    @PostMapping("/delete_post")
    public String deletePost(@RequestParam long id) {
        editorService.deletePost(id);
        return "redirect:/blog?PostDeleted";
    }

    @GetMapping("/edit_post/{id}")
    public String editPost(@PathVariable long id, Model model)   {
        Posts post = postsRepository.getById(id);
        model.addAttribute("post", post);
        model.addAttribute("categories", categoriesRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return PATH_TO_EDITOR_FOLDER + "edit_post";
    }

    @PostMapping("/edit_post")
    public String editPost(@RequestParam String title, @RequestParam String description, @RequestParam long id,
                           @RequestParam(required = false) MultipartFile image, @RequestParam long idCategory) throws IOException   {
        try {
            Posts post = editorService.editPost(id, title, description, idCategory);
            if (!image.isEmpty())  {
                String filename = uploadsPhotosService.createFilenameWithUUID(image.getOriginalFilename());
                uploadsPhotosService.saveFile(image, filename, post);
            }
            return "redirect:/blog/" + id + "?PostEdited";
        }   catch (PostNotFoundException ex)    {
            return "redirect:/blog?PostNotFound";
        }
    }

    @GetMapping("/view_suggested_posts")
    public String viewSuggestedPosts(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 6) Pageable pageable,
                                     @RequestParam(required = false) String query, Model model, HttpServletRequest request,
                                     @RequestParam(required = false) Long idCategory)  {
        try {
            Page<SuggestedPosts> posts;
            Categories category;

            if (idCategory == null) {
                category = editorService.firstCategory();
                if (query == null) {
                    posts = suggestedPostsRepository.findAll(pageable);
                } else {
                    posts = suggestedPostsRepository.findByTitleContainingIgnoreCase(query, pageable);
                    if (!posts.hasContent()) {
                        throw new NotFoundByQueryException();
                    }
                }
            }   else
            if (idCategory == 0)    {
                posts = suggestedPostsRepository.findByDescriptionContainingIgnoreCase(query, pageable);
                category = editorService.firstCategory();
            }   else {
                category = categoriesRepository.getById(idCategory);
                if (query == null) {
                    posts = suggestedPostsRepository.findByCategory(category, pageable);
                }   else {
                    posts = suggestedPostsRepository.findByCategoryAndTitleContainingIgnoreCase(category, query, pageable);
                }
            }

            model.addAttribute("posts", posts);
            model.addAttribute("currentCategory", category);

            //pagination
            model.addAttribute("pageable", pageable);
            model.addAttribute("pages", mainService.getPages(posts.getTotalPages()));
            model.addAttribute("URI_page", "/editor/view_suggested_posts?page=");
            model.addAttribute("URI_size", "&&size=" + pageable.getPageSize());
            if (query != null) {
                model.addAttribute("URI_query", "&&query=" + query);
            }

            //values necessary for work
            model.addAttribute("query", query);
            model.addAttribute("filePath", PATH_TO_SUGGESTED_POSTS_UPLOADS);
            model.addAttribute("URI", "/editor/view_suggested_posts/");
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
            return PATH_TO_EDITOR_FOLDER + "view_suggested_posts";
        }   catch (NotFoundByQueryException ex)  {
            return "redirect:/view_suggested_posts?NotFoundByQuery";
        }

    }

    @GetMapping("/view_suggested_posts/{idPost}")
    public String viewSuggestedPost(Model model, @PathVariable Long idPost, HttpServletRequest request)   {
        try {
            SuggestedPosts post = suggestedPostsRepository.getById(idPost);
            model.addAttribute("post", post);

            model.addAttribute("filePath", PATH_TO_SUGGESTED_POSTS_UPLOADS);
            model.addAttribute("projectInfo", mainService.getProjectInfo());
            model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
            return PATH_TO_EDITOR_FOLDER + "full_suggested_post";
        }   catch (Exception ex)    {
            return "";
        }
    }

    @PostMapping("/view_suggested_posts/publish")
    public String publishSuggestedPost(@RequestParam String title, @RequestParam String description,
                                       @RequestParam long idPost, @RequestParam MultipartFile image) throws IOException {
        SuggestedPosts suggestedPost = suggestedPostsRepository.getById(idPost);
        Posts post = editorService.publishSuggestedPost(suggestedPost, title, description, suggestedPost.getImage());
        if (image.isEmpty())   {
            uploadsPhotosService.transferSuggestedImageToDirPosts(suggestedPost.getImage());
        }   else {
            String filename = uploadsPhotosService.createFilenameWithUUID(image.getOriginalFilename());
            uploadsPhotosService.saveFile(image, filename, post);
        }
        suggestedPostsRepository.delete(suggestedPost);
        return "redirect:/editor/view_suggested_posts?PublishedSuggestedPost";
    }

    @PostMapping("/view_suggested_posts/delete")
    public String deleteSuggestedPost(@RequestParam long idPost)    {
        SuggestedPosts suggestedPost = suggestedPostsRepository.getById(idPost);
        suggestedPostsRepository.delete(suggestedPost);
        return "redirect:/editor/view_suggested_posts?DeletedSuggestedPost";
    }

    @GetMapping("/categories")
    public String categories(Model model)  {
        List<Categories> categories = categoriesRepository.findAll();
        model.addAttribute("categories", categories);

        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return PATH_TO_EDITOR_FOLDER + "categories";
    }

    @GetMapping("/add_category")
    public String addCategory(Model model) {
        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return PATH_TO_EDITOR_FOLDER + "add_category";
    }

    @PostMapping("add_category")
    public String addCategory(@RequestParam String title, @RequestParam String description) {
        editorService.addCategory(title, description);
        return "redirect:/editor/categories?AddedCategory";
    }

    @PostMapping("/edit_category")
    public String editCategory(@RequestParam long idCategory, Model model)   {
        Categories category = categoriesRepository.getById(idCategory);
        model.addAttribute("category", category);

        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return PATH_TO_EDITOR_FOLDER + "edit_category";
    }

    @PostMapping("/save_category")
    public String saveCategory(@RequestParam long idCategory, @RequestParam String title,
                               @RequestParam String description, Model model)   {
        editorService.editCategory(idCategory, title, description);
        return "redirect:/editor/categories?EditedCategory";
    }

    @PostMapping("/delete_category")
    public String deleteCategory(@RequestParam long idCategory) {
        editorService.deleteCategory(idCategory);
        return "redirect:/editor/categories?DeletedCategory";
    }
}