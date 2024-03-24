package com.pms.publicationmanagement.dto;

public class InstitutionDto {

    public String name;

    public String address;

    public String phoneNumber;

    public String email;

    public InstitutionDto() {
    }

    public InstitutionDto(String name, String address, String phoneNumber, String email) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    @Override
    public String toString() { return String.format("%s %s %s %s", name, address, phoneNumber, email); }
}
