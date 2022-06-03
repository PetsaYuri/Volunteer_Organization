package com.volunteer.Volunteer.Organization.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories_of_posts")
public class Categories {

    public Categories() {}

    public Categories(String title, String description) {
        this.category = title;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category, description;

    @OneToMany(mappedBy = "category")
    private List<Posts> posts;

    @OneToMany(mappedBy = "category")
    private List<SuggestedPosts> suggestedPosts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Posts> getPosts() {
        return posts;
    }

    public void setPosts(List<Posts> posts) {
        this.posts = posts;
    }

    public List<SuggestedPosts> getSuggestedPosts() {
        return suggestedPosts;
    }

    public void setSuggestedPosts(List<SuggestedPosts> suggestedPosts) {
        this.suggestedPosts = suggestedPosts;
    }
}
