package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.models.Roles;
import com.volunteer.Volunteer.Organization.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolesService {

    @Autowired
    RolesRepository rolesRepository;

    public void addNewRole(String role, String description)    {
        Roles newRole = new Roles(role, description);
        rolesRepository.save(newRole);
    }

    public Iterable<Roles> getAllRoles()    {
        return rolesRepository.findAllByOrderById();
    }
}
