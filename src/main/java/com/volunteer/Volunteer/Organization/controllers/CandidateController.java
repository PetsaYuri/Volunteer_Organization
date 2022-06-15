package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.exceptions.EmailAlreadyExistsException;
import com.volunteer.Volunteer.Organization.exceptions.NotAllowedFileFormatException;
import com.volunteer.Volunteer.Organization.exceptions.RepeatedPasswordIsInvalidException;
import com.volunteer.Volunteer.Organization.exceptions.UserNotFoundException;
import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.models.Candidates;
import com.volunteer.Volunteer.Organization.repository.UsersRepository;
import com.volunteer.Volunteer.Organization.service.*;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class CandidateController {

    @Autowired
    private UploadsPhotosService uploadsPhotos;

    @Autowired
    private UserService userService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private MainService mainService;

    @GetMapping("/anketa")
    public String anketa(Model model, HttpServletRequest request)    {
        model.addAttribute("active", "anketa");
        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());

        Users user = usersRepository.findByEmail(request.getRemoteUser());
        if (user != null) {
            model.addAttribute("role", user.getRoles().getRole());
        }
        return "anketa";
    }

    @PostMapping("/anketa")
    public String postForm(@RequestParam String name, @RequestParam String email, @RequestParam String phone,
                           @RequestParam String city, @RequestParam String description,
                           @RequestParam("file") MultipartFile file, Model model)
            throws NotAllowedFileFormatException, IOException {
        model.addAttribute("email", email);
        try {
            String filename = file.getOriginalFilename();
            if(!candidateService.isAlreadyExistsEmail(email))   {
                if(uploadsPhotos.isAllowedFileFormat(filename))   {
                    String filenameWithUUID = uploadsPhotos.createFilenameWithUUID(filename);
                    Candidates volunteer = candidateService.addVolunteer(name, email, phone, city, description);
                    uploadsPhotos.saveFile(file, filenameWithUUID, volunteer);

                    String message = candidateService.sendActivationMessageToEmail(volunteer);
                    mailSenderService.send(email, "Активація коду", message);
                } else {
                    throw new NotAllowedFileFormatException();
                }
            }   else {
                throw new EmailAlreadyExistsException();
            }
        }
        catch (FileUploadException ex) {
            return "redirect:anketa?NotAllowedFileFormatException";
        }   catch (EmailAlreadyExistsException ex)  {
            return "redirect:anketa?EmailAlreadyExistsException";
        }
        return "redirect:/?SendingMessageToEmail";
}

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) throws IOException {
        boolean isActivated =  candidateService.activateEmail(code);

        if(isActivated) {
            model.addAttribute("message", "Активація пройшла успішно");
        }
        else {
            model.addAttribute("message", "Активація не здійснилась");
        }
        model.addAttribute("projectInfo", mainService.getProjectInfo());
        model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
        return "anketa-sucessful_send";
    }

    @GetMapping("/settings_account/{link}")
    public String settingsAccount(@PathVariable String link, @RequestParam String email, Model model) {
        Users user = usersRepository.findByEmail(email);
        if (user.getPassword().equals(link))    {
            model.addAttribute("email", email);
            model.addAttribute("link", link);
            model.addAttribute("projectInfo", mainService.getProjectInfo());
            model.addAttribute("pathProjectInfo", mainService.getPathProjectInfo());
            return "settings_account";
        }
        return null;
    }

    @PostMapping("/set_password")
    public String setPassword(@RequestParam String password, @RequestParam String rePassword,
                              @RequestParam String email, @RequestParam String link) {
        try {
            if (password.equals(rePassword))    {
                userService.setPassword(email, password, link);
                return "redirect:/login";
            }   else {
                throw new RepeatedPasswordIsInvalidException();
            }
        }   catch (RepeatedPasswordIsInvalidException ex)   {
            return "redirect:/settings_account/" + link + "?RepeatedPasswordInvalid";
        }   catch (UserNotFoundException ex)    {
            return "redirect:/settings_account/" + link + "?UserNotFound";
        }
    }
}
