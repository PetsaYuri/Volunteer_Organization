package com.volunteer.Volunteer.Organization.controllers;

import com.volunteer.Volunteer.Organization.service.FormService;
import com.volunteer.Volunteer.Organization.service.MailSenderService;
import com.volunteer.Volunteer.Organization.service.UploadsPhotosService;
import com.volunteer.Volunteer.Organization.models.Form;
import com.volunteer.Volunteer.Organization.repository.FormRepository;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class FormController {

    @Autowired
    private UploadsPhotosService uploadsPhotos;

    @Autowired
    private FormService formService;

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
                           Model model) throws IOException {
            model.addAttribute("email", email);
            String activationCode = "";
        try {
            if(uploadsPhotos.checkFormatFile(file.getOriginalFilename()))   {
                uploadsPhotos.setFilename(file.getOriginalFilename());
                String photo = uploadsPhotos.createFilenameWithUUID();
                Form form = formService.createForm(name, email, phone, city, description, status, photo);
                uploadsPhotos.saveFileToServer(file);
                formService.saveForm(form);

                String message = formService.sendMessageToEmail(form);

                mailSenderService.send(email, "Активація коду", message);
            }   else {
                throw new FileUploadException();
            }
        }   catch (FileUploadException ex) {
            return "redirect:anketa/?FileFormatException";
        }

        return "anketa-sucessful_send";
}

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code)  {
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
