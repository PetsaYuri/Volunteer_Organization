package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.exceptions.EmailAlreadyExistsException;
import com.volunteer.Volunteer.Organization.exceptions.NotAllowedFileFormatException;
import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.models.Volunteers;
import com.volunteer.Volunteer.Organization.repository.UsersRepository;
import com.volunteer.Volunteer.Organization.service.MailSenderService;
import com.volunteer.Volunteer.Organization.service.UploadsPhotosService;
import com.volunteer.Volunteer.Organization.service.UserService;
import com.volunteer.Volunteer.Organization.service.VolunteerService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class VolunteerController {

    @Autowired
    private UploadsPhotosService uploadsPhotos;

    @Autowired
    private UserService userService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private MailSenderService mailSenderService;

    @GetMapping("/anketa")
    public String anketa(Model model, HttpServletRequest request)    {
        model.addAttribute("active", "anketa");

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
            if(!volunteerService.isAlreadyExistsEmail(email))   {
                if(uploadsPhotos.isAllowedFileFormat(filename))   {
                    System.out.println(file.getSize());
                    String filenameWithUUID = uploadsPhotos.createFilenameWithUUID(filename);
                    Volunteers volunteer = volunteerService.addVolunteer(name, email, phone, city, description, filenameWithUUID);
                    uploadsPhotos.saveFileToServer(file);

                    String message = userService.sendActivationMessageToEmail(volunteer);
                  //  mailSenderService.send(email, "Активація коду", message);
                } else {
                    throw new NotAllowedFileFormatException();
                }
            }   else {
                throw new EmailAlreadyExistsException();
            }
        }   catch (FileUploadException ex) {
            return "redirect:anketa?NotAllowedFileFormatException";
        }   catch (EmailAlreadyExistsException ex)  {
            return "redirect:anketa?EmailAlreadyExistsException";
        }
        return "redirect:/?SendingMessageToEmail";
}

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) throws IOException {
        boolean isActivated = userService.activateEmail(code);

        if(isActivated) {
            model.addAttribute("message", "Активація пройшла успішно");
        }
        else {
            model.addAttribute("message", "Активація не здійснилась");
        }

        return "anketa-sucessful_send";
    }
}
