package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.exceptions.IncorrectQueryException;
import com.volunteer.Volunteer.Organization.models.Volunteers;
import com.volunteer.Volunteer.Organization.repository.VolunteersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VolunteerService {

    @Autowired
    private VolunteersRepository volunteersRepository;

    public Volunteers addVolunteer(String name, String email, String phone, String city,
                                   String description)    {
        Volunteers volunteer = new Volunteers(name, email, phone, city, description);
        volunteer.setActivationCode(UUID.randomUUID().toString());
        volunteersRepository.save(volunteer);
        return volunteer;
    }

    public boolean isAlreadyExistsEmail(String email)  {
        Volunteers volunteer = volunteersRepository.findByEmail(email);

        if(volunteer == null)    {
            return false;
        }   else {
            return true;
        }
    }

    public Page<Volunteers> searchByField(String field, String query, Pageable pageable) throws IncorrectQueryException {
        Page<Volunteers> volunteers;
        switch (field)  {
            case "name":
                return volunteers = volunteersRepository.findByNameContainingIgnoreCase(query, pageable);
            case "email":
                return volunteers = volunteersRepository.findByEmailContainingIgnoreCase(query, pageable);
            case "phone":
                return volunteers = volunteersRepository.findByPhoneContainingIgnoreCase(query, pageable);
            case "city":
                return volunteers = volunteersRepository.findByCityContainingIgnoreCase(query, pageable);
            default:
                throw new IncorrectQueryException();
        }
    }
}
