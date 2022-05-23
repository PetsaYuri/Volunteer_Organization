package com.volunteer.Volunteer.Organization.repository;

import com.volunteer.Volunteer.Organization.models.Roles;
import com.volunteer.Volunteer.Organization.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);

    Iterable<Users> findAllByOrderById();

    Page<Users> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<Users> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Users> findByBlocked(Boolean blocked, Pageable pageable);

    Page<Users> findByRoles(Roles roles, Pageable pageable);
}
