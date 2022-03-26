package com.volunteer.Volunteer.Organization.controllers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.volunteer.Volunteer.Organization.models.Posts;
import com.volunteer.Volunteer.Organization.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.volunteer.Volunteer.Organization.service.UserService.PATH_TO_ADMIN_FOLDER;
import static com.volunteer.Volunteer.Organization.service.UserService.PATH_TO_EDITOR_FOLDER;

@Controller
//@RequestMapping("/editor")
public class EditorContoller {

    @Autowired
    private PostService postService;

    @GetMapping("/blog")
    public String blog(Model model)    {
        List<Posts> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        model.addAttribute("URI", "blog/");
        postService.lastPosts();
        return PATH_TO_EDITOR_FOLDER + "blog";
    }

    @GetMapping("/add_post")
    public String addPost(Model model) {
        return PATH_TO_EDITOR_FOLDER + "add_post";
    }

    @PostMapping("/add_post")
    public String addNewPost(Model model, @RequestParam String title, @RequestParam String description,
                             @RequestParam MultipartFile image)   {
        postService.addNewPost(title, description);
        return "redirect:add_post?AddedPost";
    }

    @GetMapping("/blog/{idPost}")
    public String fullPost(Model model, @PathVariable Long idPost)    {
        try {
            Optional<Posts> post = postService.getPostById(idPost);
            model.addAttribute("post", post.get());

            List<Posts> lastPosts = postService.lastPosts();
            model.addAttribute("lastPosts", lastPosts);
            return PATH_TO_EDITOR_FOLDER + "full_post";
        }   catch (Exception ex)    {
            return "";
        }
    }

    @PostMapping("/add_comment")
    public String addComment(Model model, @RequestParam String author,
                             @RequestParam String comment, @RequestParam Long id_post)  {
        postService.addNewComment(author, comment, id_post);
        return "redirect:blog/" + id_post + "?AddedComment";
    }
}
