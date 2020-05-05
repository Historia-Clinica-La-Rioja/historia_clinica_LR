package net.pladema.internation.service.internment.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BasicListedPatientBo {

    private Integer patientId;

    private Short identificationTypeId;

    private String identificationNumber;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private Short genderId;

    public BasicListedPatientBo(Integer patientId, Short identificationTypeId, String identificationNumber,
                                String firstName, String lastName, LocalDate birthDate, Short genderId){
        this.patientId = patientId;
        this.identificationTypeId = identificationTypeId;
        this.identificationNumber = identificationNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.genderId = genderId;
    }
}
