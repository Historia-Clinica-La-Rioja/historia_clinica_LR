package net.pladema.snvs.domain.patient;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PersonDataBo {

    private String firstName;

    private String lastName;

    private Short identificationTypeId;

    private String identificationNumber;

    private Integer ethnicityId;

    private LocalDate birthDate;

    private Short genderId;

    private String phoneNumber;

    private String email;

    private AddressDataBo address;

    public PersonDataBo(String firstName, String lastName, Short identificationTypeId,
                        String identificationNumber,
                        Integer ethnicityId, LocalDate birthDate,
                        Short genderId, String phoneNumber, String email,
                        AddressDataBo address) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.identificationTypeId = identificationTypeId;
        this.identificationNumber = identificationNumber;
        this.ethnicityId = ethnicityId;
        this.birthDate = birthDate;
        this.genderId = genderId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

}
