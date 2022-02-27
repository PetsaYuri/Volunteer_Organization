package com.volunteer.Volunteer.Organization.repository;

import com.volunteer.Volunteer.Organization.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    Iterable<Roles> findAllByOrderById();
}
