package net.pladema.patient.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.person.repository.entity.Person;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientPersonVo {

    private Integer id;

    private Short patientTypeId;

    private Boolean possibleDuplicate;

    private Integer nationalId;

    private String comments;

    private Short identityVerificationStatusId;

    private Integer personId;

    private String firstName;

    private String middleNames;

    private String lastName;

    private String otherLastNames;

    private Short identificationTypeId;

    private String identificationNumber;

    private Short genderId;

    private LocalDate birthDate;

    public PatientPersonVo(Patient patient, Person person) {
        this.id = patient.getId();
        this.patientTypeId = patient.getTypeId();
        this.possibleDuplicate = patient.getPossibleDuplicate();
        this.nationalId = patient.getNationalId();
        this.comments = patient.getComments();
        this.identityVerificationStatusId = patient.getIdentityVerificationStatusId();
        this.personId = person.getId();
        this.firstName = person.getFirstName();
        this.middleNames = person.getMiddleNames();
        this.lastName = person.getLastName();
        this.otherLastNames = person.getOtherLastNames();
        this.identificationTypeId = person.getIdentificationTypeId();
        this.identificationNumber = person.getIdentificationNumber();
        this.genderId = person.getGenderId();
        this.birthDate = person.getBirthDate();
    }

}
