package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.projects.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    @Query("SELECT p FROM Project p JOIN p.users u WHERE u.id = :userId ORDER BY p.createdAt DESC")
    List<Project> findProjectsByUserId(@Param("userId") UUID userId);
}
