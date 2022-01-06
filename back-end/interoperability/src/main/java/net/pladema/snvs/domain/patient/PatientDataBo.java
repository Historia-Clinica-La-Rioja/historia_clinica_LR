package net.pladema.snvs.domain.patient;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PatientDataBo {

    private final Integer id;

    private final PersonDataBo person;

    public PatientDataBo(Integer id, PersonDataBo person) {
        this.id = id;
        this.person = person;
    }

    public String getFirstName() {
        return person == null ? null :  person.getFirstName();
    }

    public String getLastName() {
        return person == null ? null :  person.getLastName();
    }
    public String getEmail() {
        return person == null ? null :  person.getEmail();
    }

    public String getPhoneNumber() {
        return person == null ? null :  person.getPhoneNumber();
    }

    public String getIdentificationNumber() {
        return person == null ? null :  person.getIdentificationNumber();
    }

    public Short getIdentificationTypeId() {
        return person == null ? null :  person.getIdentificationTypeId();
    }

    public AddressDataBo getAddress() {
        return person == null ? null :  person.getAddress();
    }

    public LocalDate getBirthDate() {
        return person == null ? null :  person.getBirthDate();
    }

    public Short getGenderId() {
        return person == null ? null :  person.getGenderId();
    }

    public Integer getEthnicityId() {
        return person == null ? null :  person.getEthnicityId();
    }
}
