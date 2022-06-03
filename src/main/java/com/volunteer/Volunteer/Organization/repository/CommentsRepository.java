package com.volunteer.Volunteer.Organization.repository;

import com.volunteer.Volunteer.Organization.models.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
}
