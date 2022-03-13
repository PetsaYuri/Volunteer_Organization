package com.volunteer.Volunteer.Organization.repository;

import com.volunteer.Volunteer.Organization.models.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    Iterable<Posts> findAllByOrderById();

    Posts findByIdOrderById(Long id);
}
