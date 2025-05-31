package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.user.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Page<Project> findAllByProjectOwnerId(UUID projectOwnerId, Pageable pageable);
}
