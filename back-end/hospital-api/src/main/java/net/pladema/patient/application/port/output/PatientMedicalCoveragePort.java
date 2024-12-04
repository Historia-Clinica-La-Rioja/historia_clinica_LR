package net.pladema.patient.application.port.output;

import net.pladema.patient.domain.GetMedicalCoverageHealthInsuranceValidationDataBo;

public interface PatientMedicalCoveragePort {

	GetMedicalCoverageHealthInsuranceValidationDataBo getMedicalCoverageHealthInsuranceValidationDataById(Integer medicalCoverageId);

}
