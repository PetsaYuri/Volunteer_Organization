package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.exceptions.EmailAlreadyExistsException;
import com.volunteer.Volunteer.Organization.exceptions.NotAllowedFileFormatException;
import com.volunteer.Volunteer.Organization.models.Form;
import com.volunteer.Volunteer.Organization.repository.FormRepository;
import com.volunteer.Volunteer.Organization.service.FormService;
import com.volunteer.Volunteer.Organization.service.MailSenderService;
import com.volunteer.Volunteer.Organization.service.UploadsPhotosService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FormController {

    @Autowired
    private UploadsPhotosService uploadsPhotos;

    @Autowired
    private FormService formService;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private MailSenderService mailSenderService;

    @GetMapping("/anketa")
    public String anketa(Model model)    {
        model.addAttribute("active", "anketa");
        return "anketa";
    }

    @PostMapping("/anketa")
    public String postForm(@RequestParam String name, @RequestParam String email, @RequestParam String phone,
                           @RequestParam String city, @RequestParam String description,
                           @RequestParam("file") MultipartFile file, @RequestParam String status,
                           Model model) throws NotAllowedFileFormatException, IOException {
            model.addAttribute("email", email);
        try {
            String filename = file.getOriginalFilename();
            if(!formService.isAlreadyExistsEmail(email))   {
                if(uploadsPhotos.isAllowedFileFormat(filename))   {
                    String filenameWithUUID = uploadsPhotos.createFilenameWithUUID(filename);
                    Form form = formService.addForm(name, email, phone, city, description, status, filenameWithUUID);
                    uploadsPhotos.saveFileToServer(file);

                    String message = formService.sendMessageToEmail(form);
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
        boolean isActivated = formService.activateForm(code);

        if(isActivated) {
            model.addAttribute("message", "Активація пройшла успішно");
        }
        else {
            model.addAttribute("message", "Активація не здійснилась");
        }

        return "anketa-sucessful_send";
    }
}
