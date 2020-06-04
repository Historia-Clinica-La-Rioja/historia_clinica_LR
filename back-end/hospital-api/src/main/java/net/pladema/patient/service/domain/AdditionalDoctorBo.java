package net.pladema.patient.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.patient.controller.dto.AAdditionalDoctorDto;
import net.pladema.patient.repository.entity.AdditionalDoctor;

@Getter
@Setter
@NoArgsConstructor
public class AdditionalDoctorBo {

    private Integer id;
    private Integer patientId;
    private String fullName;
    private String phoneNumber;
    private Boolean generalPractitioner;

    public AdditionalDoctorBo (AdditionalDoctor additionalDoctor){
        this.id = additionalDoctor.getId();
        this.patientId = additionalDoctor.getPatientId();
        this.fullName = additionalDoctor.getFullName();
        this.phoneNumber = additionalDoctor.getPhoneNumber();
        this.generalPractitioner = additionalDoctor.isGeneralPractitioner();
    }

    public AdditionalDoctorBo (AAdditionalDoctorDto aAdditionalDoctorDto) { // From FE to BE
    	this.id = aAdditionalDoctorDto.getId();
        this.fullName = aAdditionalDoctorDto.getFullName();
        this.phoneNumber = aAdditionalDoctorDto.getPhoneNumber();
        this.generalPractitioner = aAdditionalDoctorDto.isGeneralPractitioner();
    }
}
