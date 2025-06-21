package com.pms.publicationmanagement.service.user;

import com.pms.publicationmanagement.dto.AddInvitationDto;
import com.pms.publicationmanagement.model.user.Invitation;
import com.pms.publicationmanagement.repository.InvitationRepository;
import com.pms.publicationmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationService {

    @Value("${invitations.base-url}")
    private String invitationBaseUrl;

    private final InvitationRepository invitationRepository;

    public void sendInvitation(AddInvitationDto addInvitationDto) {
        Invitation invitation = new Invitation();
        UUID id = UUID.randomUUID();
        invitation.setId(id);
        invitation.setEmail(addInvitationDto.email);
        invitation.setInstitutionId(addInvitationDto.institutionId);
        invitation.setLink(invitationBaseUrl + id);
        invitation.setCreatedAt(LocalDateTime.now());

        invitationRepository.save(invitation);
    }

    public List<Invitation> findAll() {
        return invitationRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public void deleteInvitation(UUID id) {
        invitationRepository.deleteById(id);
    }

    public Invitation findById(UUID id) {
        return invitationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invitation Not Found"));
    }
}
