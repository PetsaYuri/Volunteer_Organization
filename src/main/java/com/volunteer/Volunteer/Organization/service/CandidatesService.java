package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.models.Candidates;
import com.volunteer.Volunteer.Organization.repository.CandidatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CandidatesService {

    @Autowired
    private CandidatesRepository candidatesRepository;

    @Autowired
    private MailSenderService mailSender;

    public Iterable<Candidates> findAllCandidates() {
        Iterable<Candidates> forms = candidatesRepository.findAllByOrderById();
        return forms;
    }

    public void changeStatusForCandidate(long idCandidate, String newStatus)    {
        Candidates candidates = candidatesRepository.findById(idCandidate);
        candidates.setStatus(newStatus);
        candidatesRepository.save(candidates);
    }

    public List<Candidates> checkCurrentFilter(String currentFilter)    {
        List<Candidates> forms;

        if(currentFilter.equals(""))   {
            forms = candidatesRepository.findAll();
        }   else {
            forms = candidatesRepository.findByStatusOrderById(currentFilter);
        }

        return forms;
    }

    public List<Candidates> findByFilter(String filter)    {
        List<Candidates> forms = candidatesRepository.findByStatusOrderById(filter);
        return forms;
    }

    public Candidates addCandidate(String name, String email, String phone, String city,
                                   String description, String filename)    {
        Candidates candidates = new Candidates(name, email, phone, city, description, filename);
        candidates.setActivationCode(UUID.randomUUID().toString());
        candidatesRepository.save(candidates);
        return candidates;
    }

    public String sendMessageToEmail(Candidates candidates)    {
        String message = String.format("Доброго дня %s! \n" +
                        "Будь ласка, перейдіть по посиланню ниже " +
                        "http://localhost:8081/activate/%s",
                candidates.getName(),
                candidates.getActivationCode());
        return message;
    }

    public boolean activateEmail(String code) {
        Candidates candidates = candidatesRepository.findByActivation(code);

        if(candidates == null)    {
            return false;
        }

        candidates.setActivationCode("confirmed");
        candidatesRepository.save(candidates);

        return true;
    }

    public boolean isAlreadyExistsEmail(String email)  {
        Candidates candidates = candidatesRepository.findByEmail(email);

        if(candidates == null)    {
            return false;
        }   else {
            return true;
        }
    }
}
