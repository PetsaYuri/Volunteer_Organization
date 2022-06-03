package com.volunteer.Volunteer.Organization.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class Users {

    public Users()  {}

    public Users(String password, Roles role, String email, String name)  {
        this.password = password;
        this.roles = role;
        this.email = email;
        this.name = name;
        this.blocked = false;
    }

    public Users(String password, Roles role, Candidates candidate)   {
        this.password = password;
        this.roles = role;
        this.candidate = candidate;
        this.email = candidate.getEmail();
        this.name = candidate.getName();
        this.blocked = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String password, email, name;

    @Column
    private Boolean blocked;

    @OneToOne
    @JoinColumn(name = "id_candidate")
    private Candidates candidate;

    @ManyToOne
    @JoinColumn(name = "id_roles")
    private Roles roles;

    @OneToMany(mappedBy = "user")
    private List<Posts> post;

    @OneToMany(mappedBy = "user")
    private List<Comments> comments;

    @OneToMany(mappedBy = "user")
    private List<SuggestedPosts> suggestedPosts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles role) {
        this.roles = role;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Candidates getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidates candidate) {
        this.candidate = candidate;
    }

    public String getRole() {
        return roles.getRole();
    }

    public List<Posts> getPost() {
        return post;
    }

    public void setPost(List<Posts> post) {
        this.post = post;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public List<SuggestedPosts> getSuggestedPosts() {
        return suggestedPosts;
    }

    public void setSuggestedPosts(List<SuggestedPosts> suggestedPosts) {
        this.suggestedPosts = suggestedPosts;
    }
}