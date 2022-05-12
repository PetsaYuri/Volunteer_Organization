package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.models.Posts;
import com.volunteer.Volunteer.Organization.service.EditorService;
import com.volunteer.Volunteer.Organization.service.UploadsPhotosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.volunteer.Volunteer.Organization.service.EditorService.PATH_TO_EDITOR_FOLDER;

@Controller
//@RequestMapping("/editor")
public class EditorContoller {

    @Autowired
    private EditorService editorService;

    @Autowired
    private UploadsPhotosService uploadsPhotosService;

    @GetMapping("/add_post")
    public String addPost(Model model) {
        return PATH_TO_EDITOR_FOLDER + "add_post";
    }

    @PostMapping("/add_post")
    public String addNewPost(Model model, @RequestParam String title, @RequestParam String description,
                             @RequestParam MultipartFile image) throws IOException {
        String filename = uploadsPhotosService.createFilenameWithUUID(image.getOriginalFilename());
        editorService.addNewPost(title, description, filename);
        uploadsPhotosService.saveFileToServer(image);
        return "redirect:add_post?AddedPost";
    }

    @GetMapping("/blog/{idPost}")
    public String fullPost(Model model, @PathVariable Long idPost)    {
        try {
            Optional<Posts> post = editorService.getPostById(idPost);
            model.addAttribute("post", post.get());

            List<Posts> lastPosts = editorService.lastPosts();
            model.addAttribute("lastPosts", lastPosts);
            return PATH_TO_EDITOR_FOLDER + "full_post";
        }   catch (Exception ex)    {
            return "";
        }
    }

    @PostMapping("/add_comment")
    public String addComment(Model model, @RequestParam String author,
                             @RequestParam String comment, @RequestParam Long id_post)  {
        editorService.addNewComment(author, comment, id_post);
        return "redirect:blog/" + id_post + "?AddedComment";
    }
}
