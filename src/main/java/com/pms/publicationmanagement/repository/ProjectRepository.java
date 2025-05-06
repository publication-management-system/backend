package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.user.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    Project findByTitle(String title);
}
