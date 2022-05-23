package com.volunteer.Volunteer.Organization.models;

import com.volunteer.Volunteer.Organization.service.MainService;

import javax.persistence.*;

@Entity
@Table(name = "suggested_posts")
public class SuggestedPosts {

    public SuggestedPosts() {}

    public SuggestedPosts(String title, String description, Categories category, Users user) {
        this.title = title;
        this.description = description;
        this.date = MainService.getCurrentDate();
        this.status = "waiting";
        this.image = "";
        this.category = category;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title, date, status;

    @Column(nullable = false, length = 10000)
    private String description;

    @Column
    private String image;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Categories category;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Users user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
