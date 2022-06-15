package com.volunteer.Volunteer.Organization.models;

import javax.persistence.*;

@Entity
@Table (name = "project_info")
public class ProjectInfo {

    public ProjectInfo()   {}

    public ProjectInfo(String name, String telegram, String email, String phone)    {
        this.name = name;
        this.telegram = telegram;
        this.email = email;
        this.phone = phone;
        this.image = "logo_for_navbar.jpg";
        this.logo = "logo.ico";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name, image, logo, telegram, email, phone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
