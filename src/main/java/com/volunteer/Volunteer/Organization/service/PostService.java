package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.models.Posts;
import com.volunteer.Volunteer.Organization.repository.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class PostService {

    @Autowired
    private PostsRepository postsRepository;

    public void addNewPost(String title, String description, String date)    {
        Posts post = new Posts(title, description, date);
        Posts p = postsRepository.save(post);
    }

    public String getCurrentDate() {
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

    public Iterable<Posts> getAllPosts()  {
        Iterable<Posts> posts = postsRepository.findAllByOrderById();
        return posts;
    }

    public Posts getPostById(Long id)   {
        Posts post = postsRepository.findByIdOrderById(id);
        return post;
    }
}
