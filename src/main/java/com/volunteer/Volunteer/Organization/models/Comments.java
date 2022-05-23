package com.volunteer.Volunteer.Organization.models;

import com.volunteer.Volunteer.Organization.service.EditorService;
import com.volunteer.Volunteer.Organization.service.MainService;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comments {

    public Comments() {
    }

    public Comments(String comment, Posts post, Volunteers volunteer) {
        this.comment = comment;
        this.date = MainService.getCurrentDate();
        this.post = post;
        this.volunteer = volunteer;
    }

    public Comments(String comment, Posts post, Users user) {
        this.comment = comment;
        this.date = MainService.getCurrentDate();
        this.post = post;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String comment, date;

    @ManyToOne
    @JoinColumn(name = "id_post")
    private Posts post;

    @ManyToOne
    @JoinColumn(name = "id_volunteer")
    private Volunteers volunteer;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Users user;

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

    public Volunteers getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteers volunteer) {
        this.volunteer = volunteer;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
