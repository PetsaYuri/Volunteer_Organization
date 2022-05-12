package com.volunteer.Volunteer.Organization.models;

import javax.persistence.*;

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

    public Users(String password, Roles role, Volunteers volunteer, String email, String name)   {
        this.password = password;
        this.roles = role;
        this.volunteer = volunteer;
        this.email = email;
        this.name = name;
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
    @JoinColumn(name = "id_volunteer")
    private Volunteers volunteer;

    @ManyToOne
    @JoinColumn(name = "id_roles")
    private Roles roles;

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

    public Volunteers getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteers volunteer) {
        this.volunteer = volunteer;
    }

    public String getRole() {
        return roles.getRole();
    }
}