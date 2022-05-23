package com.volunteer.Volunteer.Organization.repository;

import com.volunteer.Volunteer.Organization.models.ProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectInfoRepository extends JpaRepository<ProjectInfo, Long> {
}
