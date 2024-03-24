package com.pms.publicationmanagement.mapper;

import com.pms.publicationmanagement.dto.InstitutionDto;
import com.pms.publicationmanagement.model.Institution;

import java.util.ArrayList;
import java.util.List;

public class InstitutionDtoMapper {

    public static InstitutionDto toInstitutionDto(Institution institution) {
        InstitutionDto institutionDto = new InstitutionDto();
        institutionDto.address = institution.getAddress();
        institutionDto.name = institution.getName();
        institutionDto.email = institution.getEmail();
        institutionDto.phoneNumber = institution.getPhoneNumber();
        return institutionDto;
    }

    public static List<InstitutionDto> toInstitutionDtoList(List<Institution> institutionList) {
        List<InstitutionDto> result = new ArrayList<>();

        for(Institution i : institutionList) {
            result.add(toInstitutionDto(i));
        }

        return result;

    }
}
