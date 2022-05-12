package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.exceptions.IncorrectQueryException;
import com.volunteer.Volunteer.Organization.models.Roles;
import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.models.Volunteers;
import com.volunteer.Volunteer.Organization.repository.RolesRepository;
import com.volunteer.Volunteer.Organization.repository.UsersRepository;
import com.volunteer.Volunteer.Organization.repository.VolunteersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private MainService mainService;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private VolunteersRepository volunteersRepository;

    @Autowired
    private MailSenderService mailSender;

    public String sendActivationMessageToEmail(Volunteers volunteer)    {
        String message = String.format("Доброго дня %s! \n" +
                        "Будь ласка, перейдіть по посиланню ниже " +
                        "http://localhost:8081/activate/%s",
                volunteer.getName(),
                volunteer.getActivationCode());
        return message;
    }

    public boolean activateEmail(String code) {
        Volunteers volunteer = volunteersRepository.findByActivation(code);

        if(volunteer == null)    {
            return false;
        }

        volunteer.setActivationCode("confirmed");
        volunteersRepository.save(volunteer);

        return true;
    }

    public void addUser(String password, String sRole, String email, String name)   {
        String encodedPassword = mainService.passwordEncoder(password);
        Roles role = rolesRepository.findByRole(sRole);
        Users user = new Users(encodedPassword, role, email, name);
        usersRepository.save(user);
    }

    public void addUser(String password, String sRole, Volunteers volunteer)    {
        String encodedPassword = mainService.passwordEncoder(password);
        Roles role = rolesRepository.findByRole(sRole);
        Users user = new Users(encodedPassword, role, volunteer,
                volunteer.getEmail(), volunteer.getName());
        usersRepository.save(user);
    }

    public boolean isAlreadyExistsEmail(String email) {
        Users user = usersRepository.findByEmail(email);
        if(user == null)    {
            return false;
        }   else {
            return true;
        }
    }

    public Page<Users> searchByField(String field, String query, Pageable pageable) throws IncorrectQueryException {
        Page<Users> users;
        Roles roles;
        switch (field)  {
            case "name":
                return users = usersRepository.findByNameContainingIgnoreCase(query, pageable);
            case "email":
                return users = usersRepository.findByEmailContainingIgnoreCase(query, pageable);
            case "active":
                    return users = usersRepository.findByBlocked(false, pageable);
            case "blocked":
                return users = usersRepository.findByBlocked(true, pageable);
            case "admin":
                roles = rolesRepository.findByRole("admin");
                return users = usersRepository.findByRoles(roles, pageable);
            case "user":
                roles = rolesRepository.findByRole("user");
                return users = usersRepository.findByRoles(roles, pageable);
            case "editor":
                roles = rolesRepository.findByRole("editor");
                return users = usersRepository.findByRoles(roles, pageable);
            default:
                throw new IncorrectQueryException();
        }
    }

    public void blockUser(long id) {
        Optional<Users> user = usersRepository.findById(id);
        user.get().setBlocked(true);
        usersRepository.save(user.get());
    }

    public void unblockUser(long id) {
        Optional<Users> user = usersRepository.findById(id);
        user.get().setBlocked(false);
        usersRepository.save(user.get());
    }
}
