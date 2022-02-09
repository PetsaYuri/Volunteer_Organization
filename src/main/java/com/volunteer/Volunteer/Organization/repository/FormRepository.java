package com.volunteer.Volunteer.Organization.repository;

import com.volunteer.Volunteer.Organization.models.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface FormRepository extends JpaRepository<Form, Long> {

    Form findById(long id);

    Iterable<Form> findByStatusOrderById(String status);

    Iterable<Form> findAllByOrderById();

    Form findByActivation(String code);

    Form findByEmail(String email);
}
