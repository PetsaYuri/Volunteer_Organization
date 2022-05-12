package com.volunteer.Volunteer.Organization.repository;

import com.volunteer.Volunteer.Organization.models.Volunteers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteersRepository extends JpaRepository<Volunteers, Long> {

    Volunteers findById(long id);

    Page<Volunteers> findByNameContainingIgnoreCase(final String name, Pageable pageable);

    List<Volunteers> findByStatusOrderById(String status);

    Page<Volunteers> findByStatus(String status, Pageable pageable);

    List<Volunteers> findAllByOrderById();

    Volunteers findByActivation(String code);

    Volunteers findByEmail(String email);

    Page<Volunteers> findByEmailContainingIgnoreCase (final String email, Pageable pageable);

    Page<Volunteers> findByPhoneContainingIgnoreCase (final String phone, Pageable pageable);

    Page<Volunteers> findByCityContainingIgnoreCase (final String city, Pageable pageable);
}
