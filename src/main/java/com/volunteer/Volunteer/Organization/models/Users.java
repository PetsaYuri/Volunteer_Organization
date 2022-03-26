package com.volunteer.Volunteer.Organization.models;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class Users {

    public Users()  {}

    public Users(String username, String password, String role, String email, String name)  {
        this.username = username;
        this.password = password;
        this.roles = role;
        this.email = email;
        this.name = name;
    }

    public Users(String username, String password, String role, Candidates candidate, String email, String name)   {
        this.username = username;
        this.password = password;
        this.roles = role;
        this.candidate = candidate;
        this.email = email;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username, password, roles, email, name;

    @OneToOne()
    @JoinColumn(name = "id_candidate")
    private Candidates candidate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Candidates getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidates candidate) {
        this.candidate = candidate;
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
}