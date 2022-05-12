package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.exceptions.ItLastPageException;
import com.volunteer.Volunteer.Organization.exceptions.NotExistsNextPageException;
import com.volunteer.Volunteer.Organization.exceptions.NotFoundByQueryException;
import com.volunteer.Volunteer.Organization.models.Posts;
import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.repository.PostsRepository;
import com.volunteer.Volunteer.Organization.repository.UsersRepository;
import com.volunteer.Volunteer.Organization.service.EditorService;
import com.volunteer.Volunteer.Organization.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import static com.volunteer.Volunteer.Organization.service.UploadsPhotosService.PATH_TO_PHOTO;

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

    @GetMapping("/")
    public String main(Model model, HttpServletRequest request) {
        Users user = usersRepository.findByEmail(request.getRemoteUser());
        if (user != null) {
            model.addAttribute("role", user.getRoles().getRole());
        }
        return "main_page";
    }

    @GetMapping("/blog")
    public String blog(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 6) Pageable pageable,
                        @RequestParam(required = false) String query, Model model, HttpServletRequest request)    {
        try {
            Page<Posts> posts;
            if (query == null) {
                posts = postsRepository.findAll(pageable);
            } else {
                posts = postsRepository.findByTitleContainingIgnoreCase(query, pageable);
            }

            if (!posts.hasContent()) {
                throw new NotFoundByQueryException();
            }
            model.addAttribute("posts", posts);

            //pagination
            model.addAttribute("pages", mainService.getPages(posts.getTotalPages()));
            model.addAttribute("URI_page", "/blog?page=");
            model.addAttribute("URI_size", "&&size=" + pageable.getPageSize());
            if (query != null) {
                model.addAttribute("URI_query", "&&query=" + query);
            }

            //values necessary for work
            model.addAttribute("query", query);
            model.addAttribute("filePath", PATH_TO_PHOTO);
            model.addAttribute("URI", "blog/");

            Users user = usersRepository.findByEmail(request.getRemoteUser());
            if (user != null) {
                model.addAttribute("role", user.getRoles().getRole());
            }
            return "blog";
        }   catch (NotFoundByQueryException ex)  {
            return "redirect:/blog?NotFoundByQuery";
        }
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
                         @RequestParam(required = false) String query)  {
        return "redirect:/blog?page=" + pageable.getPageNumber() + "&&size=" + pageable.getPageSize() +
                "&&query=" + query;
    }

}
