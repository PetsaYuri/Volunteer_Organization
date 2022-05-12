package com.volunteer.Volunteer.Organization.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
public class Roles {

    public Roles() {
    }

    public Roles(String role, String description) {
        this.role = role;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String role, description;

    @OneToMany(mappedBy = "roles")
    private List<Users> user;

    public Long getId() {
        return id;
    }

    public void setId(Long id_roles) {
        this.id = id_roles;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String roles) {
        this.role = roles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Users> getUser() {
        return user;
    }

    public void setUser(List<Users> user) {
        this.user = user;
    }
}
