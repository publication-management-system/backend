package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.projects.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findAllByProjectId(UUID projectId);

    @Query("""
                SELECT t FROM Task t
                JOIN t.project p
                JOIN p.users u
                WHERE t.id = :taskId
                  AND p.id = :projectId
                  AND u.id = :userId
            """)
    Task findTaskForUser(@Param("taskId") UUID taskId, @Param("projectId") UUID projectId, @Param("userId") UUID userId);


}
