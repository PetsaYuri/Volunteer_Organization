package com.volunteer.Volunteer.Organization.models;

import com.volunteer.Volunteer.Organization.service.EditorService;
import com.volunteer.Volunteer.Organization.service.MainService;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Posts {

    public Posts()  {}

    public Posts(String title, String description, Users user, Categories category)  {
        this.title = title;
        this.description = description;
        this.date = MainService.getCurrentDate();
        this.user = user;
        this.category = category;
    }

    public Posts(String title, String description, String date, String filename, Users user, Categories category)  {
        this.title = title;
        this.description = description;
        this.date = date;
        this.image = filename;
        this.user = user;
        this.category = category;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title, date;

    @Column(nullable = false, length = 10000)
    private String description;

    @Column
    private String image;

    @OneToMany(mappedBy = "post")
    @OrderBy("id")
    private List<Comments> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Categories category;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }
}
