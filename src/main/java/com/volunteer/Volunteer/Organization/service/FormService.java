package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.models.Form;
import com.volunteer.Volunteer.Organization.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Service
public class FormService {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private MailSenderService mailSender;

    public Form createForm(String name, String email, String phone, String city, String description, String status, String photo)    {
        Form form = new Form(name, email, phone, city, description, status, photo);
        form.setActivationCode(UUID.randomUUID().toString());
        return form;
    }

    public void saveForm(Form form) {
        formRepository.save(form);
    }

    public String sendMessageToEmail(Form form)    {
        String message = String.format("Доброго дня %s! \n" +
                "Будь ласка, перейдіть по посиланню ниже " +
                "<a href=\"http://localhost:8081/activate/%s\"></a>",
                form.getName(),
                form.getActivationCode());
        return message;
    }

    public boolean activateForm(String code1) {
        Form form = formRepository.findByActivation(code1);

        if(form == null)    {
            return false;
        }

        form.setActivationCode("ПІДТВЕРДЖЕНО");
        formRepository.save(form);

        return true;
    }
}
