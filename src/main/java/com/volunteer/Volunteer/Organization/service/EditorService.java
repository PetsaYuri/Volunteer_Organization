package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.models.Comments;
import com.volunteer.Volunteer.Organization.models.Posts;
import com.volunteer.Volunteer.Organization.repository.CommentsRepository;
import com.volunteer.Volunteer.Organization.repository.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class EditorService {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    public static final String PATH_TO_EDITOR_FOLDER = "editor/";
    public static final String PATH_TO_EDITOR_HEADER = "editor_";

    public void addNewPost(String title, String description, String image)    {
        Posts post = new Posts(title, description, image);
        Posts p = postsRepository.save(post);
    }

    public static String getCurrentDate() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String currentTime = timestamp.toString();
        String[] regexByDayAndHour = currentTime.split(" ");
        String[] regexByHourAndMin = regexByDayAndHour[1].split(":");
        String hourAndMin = regexByHourAndMin[0] + ":" + regexByHourAndMin[1];
        String[] regexByDayAndMonthAndYear = regexByDayAndHour[0].split("-");
        String dayAndMonthAndYear = regexByDayAndMonthAndYear[2] + "."
                + regexByDayAndMonthAndYear[1] + "." + regexByDayAndMonthAndYear[0];
        String date = hourAndMin + ", " + dayAndMonthAndYear;
        return date;
    }

    public List<Posts> getAllPosts()  {
        List<Posts> posts = postsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return posts;
    }

    public Optional<Posts> getPostById(Long id)   {
        Optional<Posts> post = postsRepository.findById(id);
        return post;
    }

    public void addNewComment(String author, String comment, Long id_post)  {
        Optional<Posts> post = postsRepository.findById(id_post);
        Comments newComment = new Comments(author, comment, post.get());
        commentsRepository.save(newComment);
    }

    public List<Posts> lastPosts()  {
        List<Posts> posts = postsRepository.findByOrderByIdDesc(PageRequest.of(0,3));
        return posts;
    }
}
