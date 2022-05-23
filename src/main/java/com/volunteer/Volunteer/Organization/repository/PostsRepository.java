package com.volunteer.Volunteer.Organization.repository;

import com.volunteer.Volunteer.Organization.models.Categories;
import com.volunteer.Volunteer.Organization.models.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    Optional<Posts> findById(Long id);

    Page<Posts> findByCategory(Categories category, Pageable pageable);

    Page<Posts> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Posts> findByCategoryAndTitleContainingIgnoreCase(Categories category, String title, Pageable pageable);

    Page<Posts> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
}
