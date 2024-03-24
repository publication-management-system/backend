package com.pms.publicationmanagement.dto;

public class AddInstitutionDto {

    public String name;

    public String address;

    public String phoneNumber;

    public String email;

    public AddInstitutionDto() {
    }

    public AddInstitutionDto(String name, String address, String phoneNumber, String email) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
