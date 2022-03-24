package com.volunteer.Volunteer.Organization.models;

import com.volunteer.Volunteer.Organization.service.PostService;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comments {

    public Comments() {
    }

    public Comments(String author, String comment, Posts post) {
        this.author = author;
        this.comment = comment;
        this.date = PostService.getCurrentDate();
        this.post = post;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String comment, author, date;

    @ManyToOne
    @JoinColumn(name = "id_post")
    private Posts post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Posts getPost() {
        return post;
    }

    public void setPost(Posts post) {
        this.post = post;
    }
}
