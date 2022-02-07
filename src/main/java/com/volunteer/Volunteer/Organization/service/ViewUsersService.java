package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViewUsersService {

    @Autowired
    private UsersRepository usersRepository;

    public Iterable<Users> findAllUsers()  {
        Iterable<Users> user = usersRepository.findAllByOrderById();
        return user;
    }
}
