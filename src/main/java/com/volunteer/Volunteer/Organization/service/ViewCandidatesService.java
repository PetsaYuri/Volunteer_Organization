package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.models.Form;
import com.volunteer.Volunteer.Organization.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViewCandidatesService {

    @Autowired
    private FormRepository formRepository;

    public Iterable<Form> findAllCandidates() {
        Iterable<Form> forms = formRepository.findAllByOrderById();
        return forms;
    }

    public void changeStatusForCandidate(long idCandidate, String newStatus)    {
        Form form = formRepository.findById(idCandidate);
        form.setStatus(newStatus);
        formRepository.save(form);
    }

    public Iterable<Form> checkCurrentFilter(String currentFilter)    {
        Iterable<Form> forms;

        if(currentFilter.equals(""))   {
            forms = formRepository.findAll();
        }   else {
            forms = formRepository.findByStatusOrderById(currentFilter);
        }

        return forms;
    }

    public Iterable<Form> findByFilter(String filter)    {
        Iterable<Form> forms = formRepository.findByStatusOrderById(filter);
        return forms;
    }
}
