package com.volunteer.Volunteer.Organization.models;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class Users {

    public Users()  {}

    public Users(String username, String password, String role)  {
        this.username = username;
        this.password = password;
        this.roles = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username, password, roles;

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
}