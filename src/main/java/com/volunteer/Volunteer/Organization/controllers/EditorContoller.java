package com.volunteer.Volunteer.Organization.controllers;

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

import static com.volunteer.Volunteer.Organization.service.UserService.PATH_TO_EDITOR_FOLDER;

@Controller
public class EditorContoller {

    @Autowired
    private PostService postService;

    @GetMapping("/blog")
    public String blog(Model model)    {
        Iterable<Posts> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        model.addAttribute("URI", "blog/");
        return PATH_TO_EDITOR_FOLDER + "blog";
    }

    @GetMapping("/add_post")
    public String addPost(Model model) {
        return PATH_TO_EDITOR_FOLDER + "add_post";
    }

    @PostMapping("/add_post")
    public String addNewPost(Model model, @RequestParam String title, @RequestParam String description,
                             @RequestParam MultipartFile image)   {
        String date = postService.getCurrentDate();
        postService.addNewPost(title, description, date);
        return "redirect:add_post?AddedPost";
    }

    @GetMapping("/blog/{idPost}")
    public String fullPost(Model model, @PathVariable String idPost)    {
        long l = Long.parseLong(idPost);
        Posts post = postService.getPostById(l);
        model.addAttribute("post", post);
        return PATH_TO_EDITOR_FOLDER + "full_post";
    }
}
