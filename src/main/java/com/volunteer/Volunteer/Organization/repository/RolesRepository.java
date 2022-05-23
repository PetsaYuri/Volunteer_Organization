package com.volunteer.Volunteer.Organization.repository;

import com.volunteer.Volunteer.Organization.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    Iterable<Roles> findAllByOrderById();

    Roles findByRole(String role);
}
