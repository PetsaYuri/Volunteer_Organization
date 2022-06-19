package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.exceptions.CandidateNotFoundException;
import com.volunteer.Volunteer.Organization.exceptions.CurrentPassIsInvalid;
import com.volunteer.Volunteer.Organization.exceptions.IncorrectQueryException;
import com.volunteer.Volunteer.Organization.exceptions.UserNotFoundException;
import com.volunteer.Volunteer.Organization.models.Roles;
import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.models.Candidates;
import com.volunteer.Volunteer.Organization.repository.RolesRepository;
import com.volunteer.Volunteer.Organization.repository.UsersRepository;
import com.volunteer.Volunteer.Organization.repository.CandidatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Value("${url.name}")
    private String url;

    @Autowired
    private MainService mainService;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CandidatesRepository candidatesRepository;

    @Autowired
    private MailSenderService mailSender;

    public String sendAcceptanceMessage(long idCandidate)  {
        Candidates candidate = candidatesRepository.getById(idCandidate);
        Users user = usersRepository.findByCandidate(candidate);
        String message = String.format("Доброго дня %s! \n" +
                    "Ми прийняли вашу заявку! Для створення облікового запису перейдіть за посиланням: %s", candidate.getName(),
                url + "/settings_account/" + user.getPassword() + "?email=" + candidate.getEmail());
        return message;
    }

    public String sendDenialMessage(long idCandidate)    {
        Candidates candidate = candidatesRepository.getById(idCandidate);
        String message = String.format("Доброго дня %s! \n" +
                "Вам було вирішено відмовити по певним причинам", candidate.getName());
        return message;
    }

    public String sendRecoveryMessage(Users user)   {
        String message = String.format("Доброго дня %s! \n" +
                "Нам прийшло повідомлення про те, що ви втратили доступ до свого облікового запису. " +
                        "Якщо це так, то просимо перейти за посиланням для відновлення доступу: %s . " +
                        "Якщо ні, то просто ігноруйте цей лист!", user.getName(),
                url + "/settings_account/" + user.getPassword() + "?email=" + user.getEmail());
        return message;
    }

    public void setPassword(String email, String password, String link) throws UserNotFoundException  {
        Users user = usersRepository.findByEmail(email);
        if (usersRepository.existsById(user.getId()) && user.getPassword().equals(link)) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String encodedPass = bCryptPasswordEncoder.encode(password);
            user.setPassword(encodedPass);
            usersRepository.save(user);
        }   else {
            throw new UserNotFoundException();
        }
    }

    public Users verification(String email) {
        Users user = usersRepository.findByEmail(email);
        if (usersRepository.existsById(user.getId())) {
            String link = UUID.randomUUID().toString();
            user.setPassword(link);
            usersRepository.save(user);
        }
        return user;
    }

    public Roles changePassword(HttpServletRequest request, String curPassword, String newPassword)
            throws CurrentPassIsInvalid, UserNotFoundException  {
        Users user = usersRepository.findByEmail(request.getRemoteUser());
        if (usersRepository.existsById(user.getId()))   {
            BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
            if (bCryptEncoder.matches(curPassword, user.getPassword())) {
                String encodedPass = bCryptEncoder.encode(newPassword);
                user.setPassword(encodedPass);
                usersRepository.save(user);
            }   else {
                throw new CurrentPassIsInvalid();
            }
            return user.getRoles();
        }   else {
            throw new UserNotFoundException();
        }
    }

    public void addUser(String password, String sRole, String email, String name)   {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        Roles role = rolesRepository.findByRole(sRole);
        Users user = new Users(encodedPassword, role, email, name);
        usersRepository.save(user);
    }

    public void addUser(String password, String sRole, Candidates candidate)    {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        Roles role = rolesRepository.findByRole(sRole);
        Users user = new Users(encodedPassword, role, candidate);
        usersRepository.save(user);
    }

    public Users addUser(String role, Candidates candidate, String link)   {
        Roles roles = rolesRepository.findByRole(role);
        Users user = new Users(link, roles, candidate);
        usersRepository.save(user);
        return user;
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

    public Users acceptUser(long id)  throws CandidateNotFoundException  {
        Candidates candidate = candidatesRepository.findById(id);
        if (candidate != null) {
            candidate.setStatus("accept");
            candidatesRepository.save(candidate);

            String link = UUID.randomUUID().toString();
            Users user = addUser("user", candidate, link);
            return user;
        } else {
            throw new CandidateNotFoundException();
        }
    }

    public Candidates denyUser(long id)  {
        Candidates candidate = candidatesRepository.findById(id);
        candidate.setStatus("deny");
        candidatesRepository.save(candidate);
        return candidate;
    }
}
