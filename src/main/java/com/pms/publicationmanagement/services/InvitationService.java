package com.pms.publicationmanagement.services;

import com.pms.publicationmanagement.model.Invitation;
import com.pms.publicationmanagement.repository.SpringJpaInvitationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitationService {

    private final SpringJpaInvitationRepository invitationRepository;

    public InvitationService(SpringJpaInvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    public Invitation addInvitattion(String link) {
        Invitation saved = new Invitation(null, link, false);
        invitationRepository.save(saved);
        return saved;
    }

    public List<Invitation> findAll() {
        return invitationRepository.findAll();
    }

    public void deleteInvitation(Integer id) {
        invitationRepository.deleteById(id);
    }

    public List<Invitation> findAvailable(Boolean wasTaken) {
        return invitationRepository.findByWasTaken(wasTaken);
    }
}
