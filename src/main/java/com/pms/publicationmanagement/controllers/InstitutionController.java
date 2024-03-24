package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.AddInstitutionDto;
import com.pms.publicationmanagement.dto.InstitutionDto;
import com.pms.publicationmanagement.mapper.InstitutionDtoMapper;
import com.pms.publicationmanagement.services.InstitutionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/institutions")
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @PostMapping
    public void addInstitution(@RequestBody AddInstitutionDto addInstitutionDto) {
        institutionService.addInstitution(addInstitutionDto.name, addInstitutionDto.address, addInstitutionDto.phoneNumber, addInstitutionDto.email);

    }

    @GetMapping("/by-name-and-address")
    public InstitutionDto searchInstitutionByNameAndAddress(@RequestParam String name, @RequestParam String address) {
        return InstitutionDtoMapper.toInstitutionDto(institutionService.findByNameAndAddress(name, address));
    }

    @GetMapping("/by-email")
    public InstitutionDto searchInstitutionByEmail(@RequestParam String email) {
        return InstitutionDtoMapper.toInstitutionDto(institutionService.findByEmail(email));
    }

    @GetMapping("/by-phonenumber")
    public InstitutionDto searchInstitutionByPhoneNumber(@RequestParam String phoneNumber) {
        return InstitutionDtoMapper.toInstitutionDto(institutionService.findByPhoneNumber(phoneNumber));
    }

    @GetMapping
    public List<InstitutionDto> findAllInstitutions() {
        return InstitutionDtoMapper.toInstitutionDtoList(institutionService.findAll());
    }

    @DeleteMapping
    public void deleteInstitutionByNameAndAddress(@RequestParam String name, @RequestParam String address) {
        institutionService.deleteByNameAndAddress(name, address);
    }


}
