package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.user.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findAllByProjectId(UUID projectId);

}
