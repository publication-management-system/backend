package com.pms.publicationmanagement.service.user;

import com.pms.publicationmanagement.dto.AddInvitationDto;
import com.pms.publicationmanagement.model.user.Invitation;
import com.pms.publicationmanagement.repository.InvitationRepository;
import com.pms.publicationmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;

    private final UserRepository userRepository;

    public void sendInvitation(AddInvitationDto addInvitationDto) {
        Invitation invitation = new Invitation();
        invitation.setId(UUID.randomUUID());
        invitation.setEmail(addInvitationDto.email);
        invitation.setInstitutionId(addInvitationDto.institutionId);
        invitation.setLink("http://localhost:5173/invitation/" + invitation.getId());

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
