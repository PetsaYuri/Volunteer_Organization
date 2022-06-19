package com.volunteer.Volunteer.Organization.repository;

import com.volunteer.Volunteer.Organization.models.Candidates;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatesRepository extends JpaRepository<Candidates, Long> {

    Candidates findById(long id);

    Page<Candidates> findByNameContainingIgnoreCaseAndStatus(final String name, final String status, Pageable pageable);

    List<Candidates> findByStatusOrderById(String status);

    Page<Candidates> findByStatus(String status, Pageable pageable);

    List<Candidates> findAllByOrderById();

    Candidates findByActivation(String code);

    Candidates findByEmail(String email);

    Page<Candidates> findByEmailContainingIgnoreCaseAndStatus (final String email, final String status, Pageable pageable);

    Page<Candidates> findByPhoneContainingIgnoreCaseAndStatus (final String phone, String status, Pageable pageable);

    Page<Candidates> findByCityContainingIgnoreCaseAndStatus (final String city, String status, Pageable pageable);

    Page<Candidates> findByActivationContainingIgnoreCaseAndStatus (final String activation, String status, Pageable pageable);
}
