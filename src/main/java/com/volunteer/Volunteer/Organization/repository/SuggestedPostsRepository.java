package com.volunteer.Volunteer.Organization.repository;

import com.volunteer.Volunteer.Organization.models.Categories;
import com.volunteer.Volunteer.Organization.models.SuggestedPosts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestedPostsRepository extends JpaRepository<SuggestedPosts, Long> {

    Page<SuggestedPosts> findByCategory(Categories category, Pageable pageable);

    Page<SuggestedPosts> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<SuggestedPosts> findByCategoryAndTitleContainingIgnoreCase(Categories category, String title, Pageable pageable);

    Page<SuggestedPosts> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
}
