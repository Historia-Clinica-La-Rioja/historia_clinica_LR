package net.pladema.patient.controller.dto;

import lombok.*;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.repository.entity.PatientType;
import net.pladema.person.controller.dto.BasicDataPersonDto;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CompletePatientDto extends BasicPatientDto {

    private String medicalCoverageName;
    private String medicalCoverageAffiliateNumber;
    private PatientType patientType;

    public CompletePatientDto(Patient patient, PatientType patientType, BasicDataPersonDto personData) {
        super(patient.getId(),personData);
        this.medicalCoverageName = patient.getMedicalCoverageName();
        this.medicalCoverageAffiliateNumber = patient.getMedicalCoverageAffiliateNumber();

        this.patientType = new PatientType();
        this.patientType.setId(patientType.getId());
        this.patientType.setDescription(patientType.getDescription());
    }
}
