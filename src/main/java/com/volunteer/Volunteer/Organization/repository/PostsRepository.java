package com.volunteer.Volunteer.Organization.repository;

import com.volunteer.Volunteer.Organization.models.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    Optional<Posts> findById(Long id);

    List<Posts> findByOrderByIdDesc(Pageable pageable);

    Page<Posts> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
