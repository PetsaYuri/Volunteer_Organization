package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.exceptions.UserAlreadyExistsException;
import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    private static String currentRole = "guest";

    public static final String PATH_TO_ADMIN_FOLDER = "admin/";
    public static final String PATH_TO_ADMIN_HEADER = "admin_";

    public static final String PATH_TO_EDITOR_FOLDER = "editor/";
    public static final String PATH_TO_EDITOR_HEADER = "editor_";

    public static final String PATH_TO_USER_FOLDER = "user/";
    public static final String PATH_TO_USER_HEADER = "user_";

    public static final String PATH_TO_BLOCKS = "blocks" + File.separator;

    public static final String PATH_TO_PHOTO = "/icon/uploads/candidates/";

    public void addUser(String username, String password, String role) throws UserAlreadyExistsException {
        if(!isAlreadyExistsUser(username)) {
            String encodedPassword = passwordEncoder(password);
            Users user = new Users(username, encodedPassword, role);
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

    public static String getCurrentRole() {
        return currentRole;
    }

    public static void setCurrentRole(String currentRole) {
        UserService.currentRole = currentRole;
    }
}