package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.user.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
        Invitation findInvitationById(UUID id);

        List<Invitation> findByWasTaken(Boolean wasTaken);
}
