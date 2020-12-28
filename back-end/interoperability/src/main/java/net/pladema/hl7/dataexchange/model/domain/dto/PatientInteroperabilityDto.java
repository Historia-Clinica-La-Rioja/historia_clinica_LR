package net.pladema.hl7.dataexchange.model.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.pladema.hl7.dataexchange.model.domain.PatientVo;

@Getter
@NoArgsConstructor
public class PatientInteroperabilityDto {

    public PatientInteroperabilityDto(PatientVo patientVo){
        this.identificationNumber = patientVo.getIdentificationNumber();
        this.id = patientVo.getId();
        this.lastname = patientVo.getLastname();
        this.otherLastName = patientVo.getOtherLastName();
        this.firstname = patientVo.getFirstname();
        this.middlenames = patientVo.getMiddlenames();
        this.fullAddress = new FhirAddressDto(patientVo.getFullAddress());
        this.phoneNumber = patientVo.getPhoneNumber();
        this.birthdate = patientVo.getBirthdate();
        this.gender = patientVo.getGender();
    }

    private String identificationNumber;

    //local (domain) identifier value
    private String id;

    private String lastname;

    private String otherLastName;

    private String firstname;

    private String middlenames;

    private FhirAddressDto fullAddress;

    private String phoneNumber;

    private String birthdate;

    private String gender;
}
