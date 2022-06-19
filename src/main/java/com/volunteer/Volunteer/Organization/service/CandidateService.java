package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.exceptions.IncorrectQueryException;
import com.volunteer.Volunteer.Organization.models.Candidates;
import com.volunteer.Volunteer.Organization.repository.CandidatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CandidateService {

    @Value("${url.name}")
    private String url;

    @Autowired
    private CandidatesRepository candidatesRepository;

    public String sendActivationMessageToEmail(Candidates candidate)    {
        String message = String.format("Доброго дня %s! \n" +
                        "Будь ласка, перейдіть по посиланню ниже " + url +
                        "/activate/%s",
                candidate.getName(),
                candidate.getActivationCode());
        return message;
    }

    public boolean activateEmail(String code) {
        Candidates candidate = candidatesRepository.findByActivation(code);

        if(candidate == null)    {
            return false;
        }

        candidate.setActivationCode("confirmed");
        candidatesRepository.save(candidate);

        return true;
    }

    public Candidates addVolunteer(String name, String email, String phone, String city,
                                   String description)    {
        Candidates volunteer = new Candidates(name, email, phone, city, description);
        volunteer.setActivationCode(UUID.randomUUID().toString());
        candidatesRepository.save(volunteer);
        return volunteer;
    }

    public boolean isAlreadyExistsEmail(String email)  {
        Candidates volunteer = candidatesRepository.findByEmail(email);

        if(volunteer == null)    {
            return false;
        }   else {
            return true;
        }
    }

    public Page<Candidates> searchByField(String field, String query, String filter, Pageable pageable) throws IncorrectQueryException {
        Page<Candidates> volunteers;
        switch (field)  {
            case "name":
                return volunteers = candidatesRepository.findByNameContainingIgnoreCaseAndStatus(query, filter, pageable);
            case "email":
                return volunteers = candidatesRepository.findByEmailContainingIgnoreCaseAndStatus(query, filter, pageable);
            case "phone":
                return volunteers = candidatesRepository.findByPhoneContainingIgnoreCaseAndStatus(query, filter, pageable);
            case "city":
                return volunteers = candidatesRepository.findByCityContainingIgnoreCaseAndStatus(query, filter, pageable);
            case "activation":
                return volunteers = candidatesRepository.findByActivationContainingIgnoreCaseAndStatus("confirmed", filter, pageable);
            default:
                throw new IncorrectQueryException();
        }
    }
}
