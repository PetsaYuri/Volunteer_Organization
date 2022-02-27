package com.volunteer.Volunteer.Organization.models;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Roles {

    public Roles()  {}

    public Roles(String role, String description)   {
        this.roles = role;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String roles, description;

    public Long getId() {
        return id;
    }

    public void setId(Long id_roles) {
        this.id = id_roles;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
