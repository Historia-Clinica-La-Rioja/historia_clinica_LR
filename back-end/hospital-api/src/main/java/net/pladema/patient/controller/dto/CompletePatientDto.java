package net.pladema.patient.controller.dto;

import javax.annotation.Nullable;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.repository.entity.PatientType;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CompletePatientDto extends BasicPatientDto {

    private String medicalCoverageName;
    private String medicalCoverageAffiliateNumber;
    private PatientType patientType;
    @Nullable
    private AAdditionalDoctorDto generalPractitioner;
    @Nullable
    private AAdditionalDoctorDto pamiDoctor;

    public CompletePatientDto(Patient patient, PatientType patientType, BasicDataPersonDto personData) {
        super(patient.getId(),personData,patient.getTypeId());
        this.patientType = new PatientType();
        this.patientType.setId(patientType.getId());
        this.patientType.setDescription(patientType.getDescription());
    }

	public CompletePatientDto(Patient patient, PatientType patientType, BasicDataPersonDto personData,
			AAdditionalDoctorDto generalPractitioner, AAdditionalDoctorDto pamiDoctor) {
		this(patient, patientType, personData);
		this.generalPractitioner = generalPractitioner;
		this.pamiDoctor = pamiDoctor;
	}
}
