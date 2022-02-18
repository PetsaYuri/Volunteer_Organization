package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.models.Form;
import com.volunteer.Volunteer.Organization.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FormService {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private MailSenderService mailSender;

    public Form addForm(String name, String email, String phone, String city, String description, String status, String filename)    {
        Form form = new Form(name, email, phone, city, description, status, filename);
        form.setActivationCode(UUID.randomUUID().toString());
        formRepository.save(form);
        return form;
    }

    public String sendMessageToEmail(Form form)    {
        String message = String.format("Доброго дня %s! \n" +
                "Будь ласка, перейдіть по посиланню ниже " +
                "http://localhost:8081/activate/%s",
                form.getName(),
                form.getActivationCode());
        return message;
    }

    public boolean activateForm(String code) {
        Form form = formRepository.findByActivation(code);

        if(form == null)    {
            return false;
        }

        form.setActivationCode("ПІДТВЕРДЖЕНО");
        formRepository.save(form);

        return true;
    }

    public boolean isAlreadyExistsEmail(String email)  {
        Form form = formRepository.findByEmail(email);

        if(form == null)    {
            return false;
        }   else {
            return true;
        }
    }
}
