package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.exceptions.UserAlreadyExistsException;
import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    public void addUser(String username, String password) throws UserAlreadyExistsException {
        if(!isAlreadyExistsUser(username)) {
            String encodedPassword = passwordEncoder(password);
            Users user = new Users(username, encodedPassword);
            usersRepository.save(user);
        }   else {
            throw new UserAlreadyExistsException();
        }
    }

    public String passwordEncoder(String password)    {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        return encodedPassword;
    }

    public boolean isAlreadyExistsUser(String username) {
        Users user = usersRepository.findByUsername(username);
        if(user == null)    {
            return false;
        }   else {
            return true;
        }
    }

    public Iterable<Users> findAllUsers()  {
        Iterable<Users> user = usersRepository.findAllByOrderById();
        return user;
    }
}
