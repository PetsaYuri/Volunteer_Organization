package com.volunteer.Volunteer.Organization.repository;

import com.volunteer.Volunteer.Organization.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

    Iterable<Users> findAllByOrderById();

}
