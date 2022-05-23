package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.exceptions.PostNotFoundException;
import com.volunteer.Volunteer.Organization.exceptions.UserNotFoundException;
import com.volunteer.Volunteer.Organization.exceptions.VolunteerNotFoundException;
import com.volunteer.Volunteer.Organization.models.*;
import com.volunteer.Volunteer.Organization.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class EditorService {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private SuggestedPostsRepository suggestedPostsRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private VolunteersRepository volunteersRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    public static final String PATH_TO_EDITOR_FOLDER = "editor/";

    public Posts addNewPost(String title, String description, HttpServletRequest request,
                            long idCategory) throws UserNotFoundException   {
        Users user = usersRepository.findByEmail(request.getRemoteUser());
        if (user != null) {
            Categories category = categoriesRepository.getById(idCategory);
            Posts post = new Posts(title, description, user, category);
            postsRepository.save(post);
            return post;
        }   else {
            throw new UserNotFoundException();
        }
    }

    public Optional<Posts> getPostById(Long id)   {
        Optional<Posts> post = postsRepository.findById(id);
        return post;
    }

    public void addComment(String comment, Long id_post, HttpServletRequest request) throws VolunteerNotFoundException {
        Optional<Posts> post = postsRepository.findById(id_post);
        Volunteers volunteer = volunteersRepository.findByEmail(request.getRemoteUser());
        if (volunteer != null) {
            Comments newComment = new Comments(comment, post.get(), volunteer);
            commentsRepository.save(newComment);
        }   else {
            Users user = usersRepository.findByEmail(request.getRemoteUser());
            if (user != null)   {
                Comments newComment = new Comments(comment, post.get(), user);
                commentsRepository.save(newComment);
            }   else {
                throw new VolunteerNotFoundException();
            }
        }
    }

    public Page<Posts> lastPosts(Categories category, Pageable pageable)  {
        Page<Posts> posts = postsRepository.findByCategory(category, pageable);
        return posts;
    }

    public void deletePost(long id)    {
        Posts post = postsRepository.getById(id);
        List<Comments> comments = post.getComments();
        if (comments != null) {
            commentsRepository.deleteAll(comments);
        }
        postsRepository.delete(post);
    }

    public Posts editPost(long id, String title, String description, long idCategory) throws PostNotFoundException {
        Posts post = postsRepository.getById(id);
        if (post != null)  {
            post.setTitle(title);
            post.setDescription(description);
            Categories category = categoriesRepository.getById(idCategory);
            post.setCategory(category);
            postsRepository.save(post);
            return post;
        }   else {
            throw new PostNotFoundException();
        }
    }

    public void deleteComment(long id) {
        Comments comment = commentsRepository.getById(id);
        commentsRepository.delete(comment);
    }

    public void editComment(long id, String comment)    {
        Comments newComment = commentsRepository.getById(id);
        newComment.setComment(comment);
        commentsRepository.save(newComment);
    }

    public Categories firstCategory()    {
        List<Categories> categories = categoriesRepository.findAll();
        Categories firstCategory = categories.get(0);
        return firstCategory;
    }

    public SuggestedPosts addSuggestedPost(String title, String description, long idCategory, HttpServletRequest request)  {
        Categories category = categoriesRepository.getById(idCategory);
        Users user = usersRepository.findByEmail(request.getRemoteUser());
        SuggestedPosts post = new SuggestedPosts(title, description, category, user);
        suggestedPostsRepository.save(post);
        return post;
    }

    public Posts publishSuggestedPost(SuggestedPosts suggestedPost, String title, String description, String filename)  {
        Posts post = new Posts(title, description, suggestedPost.getDate(), filename, suggestedPost.getUser(), suggestedPost.getCategory());
        postsRepository.save(post);
        return post;
    }
}