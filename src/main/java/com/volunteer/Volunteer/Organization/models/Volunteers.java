package com.volunteer.Volunteer.Organization.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "volunteers")
public class Volunteers {

    public Volunteers()   {}

    public Volunteers(String name, String email, String phone, String city, String description) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.description = description;
        this.status = "waiting";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name, email, phone, city, description, photo, status, activation;

    @OneToOne(mappedBy = "volunteer")
    private Users user;

    @OneToMany(mappedBy = "volunteer")
    private List<Comments> comments;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        name = Name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String filename) {
        this.photo = filename;
    }

    public String getStatus()   {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivationCode()   {
        return activation;
    }

    public void setActivationCode(String activationCode) {
        this.activation = activationCode;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }


}


