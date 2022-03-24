package com.volunteer.Volunteer.Organization.repository;

import com.volunteer.Volunteer.Organization.models.Comments;
import com.volunteer.Volunteer.Organization.models.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
 //   Posts findByPost(Long id);

 //   List<Comments> findAll();

}
