package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.AddInstitutionDto;
import com.pms.publicationmanagement.dto.InstitutionDto;
import com.pms.publicationmanagement.mapper.InstitutionDtoMapper;
import com.pms.publicationmanagement.service.institution.InstitutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void addInstitution(@RequestBody AddInstitutionDto addInstitutionDto) {
        institutionService.addInstitution(addInstitutionDto.name, addInstitutionDto.address,
                addInstitutionDto.phoneNumber, addInstitutionDto.email);

    }

    @GetMapping("/by-name-and-address")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public InstitutionDto searchInstitutionByNameAndAddress(@RequestParam String name, @RequestParam String address) {
        return InstitutionDtoMapper.toInstitutionDto(institutionService.findByNameAndAddress(name, address));
    }

    @GetMapping("/by-email")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public InstitutionDto searchInstitutionByEmail(@RequestParam String email) {
        return InstitutionDtoMapper.toInstitutionDto(institutionService.findByEmail(email));
    }

    @GetMapping("/by-phonenumber")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public InstitutionDto searchInstitutionByPhoneNumber(@RequestParam String phoneNumber) {
        return InstitutionDtoMapper.toInstitutionDto(institutionService.findByPhoneNumber(phoneNumber));
    }

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<InstitutionDto> findAllInstitutions() {
        return InstitutionDtoMapper.toInstitutionDtoList(institutionService.findAll());
    }

    @DeleteMapping
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void deleteInstitutionByNameAndAddress(@RequestParam String name, @RequestParam String address) {
        institutionService.deleteByNameAndAddress(name, address);
    }

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public InstitutionDto getById(@PathVariable UUID id) {
        return InstitutionDtoMapper.toInstitutionDto(institutionService.findById(id));
    }
}
