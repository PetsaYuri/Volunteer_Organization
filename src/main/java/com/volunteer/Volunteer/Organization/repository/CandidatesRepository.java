package com.volunteer.Volunteer.Organization.repository;

import com.volunteer.Volunteer.Organization.models.Candidates;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.OrderBy;
import java.util.List;

public interface CandidatesRepository extends JpaRepository<Candidates, Long> {

    Candidates findById(long id);

    List<Candidates> findByStatusOrderById(String status);

    List<Candidates> findAllByOrderById();

    Candidates findByActivation(String code);

    Candidates findByEmail(String email);
}
