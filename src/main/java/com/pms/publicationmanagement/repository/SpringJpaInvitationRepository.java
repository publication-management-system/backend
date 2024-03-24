package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringJpaInvitationRepository extends JpaRepository<Invitation, Integer> {

        Invitation findInvitationById(Integer id);

        List<Invitation> findByWasTaken(Boolean wasTaken);


}
