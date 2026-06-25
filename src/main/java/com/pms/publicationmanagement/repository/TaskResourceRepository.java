package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.projects.TaskResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskResourceRepository extends JpaRepository<TaskResource, UUID> {
    TaskResource findByIdAndTaskId(UUID id, UUID taskId);
}
