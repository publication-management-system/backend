package com.pms.publicationmanagement.service.user;

import com.pms.publicationmanagement.model.user.Invitation;
import com.pms.publicationmanagement.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;

    public void sendInvitation(String link) {
        Invitation invitation = new Invitation();
        invitation.setLink(link);
        invitation.setWasTaken(false);

        invitationRepository.save(invitation);
    }

    public List<Invitation> findAll() {
        return invitationRepository.findAll();
    }

    public void deleteInvitation(UUID id) {
        invitationRepository.deleteById(id);
    }

    public List<Invitation> findAvailable(Boolean wasTaken) {
        return invitationRepository.findByWasTaken(wasTaken);
    }
}
