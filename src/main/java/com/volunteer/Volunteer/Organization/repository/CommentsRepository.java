package com.volunteer.Volunteer.Organization.repository;

import com.volunteer.Volunteer.Organization.models.Comments;
import com.volunteer.Volunteer.Organization.models.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
 //   Posts findByPost(Long id);

 //   List<Comments> findAll();

}
