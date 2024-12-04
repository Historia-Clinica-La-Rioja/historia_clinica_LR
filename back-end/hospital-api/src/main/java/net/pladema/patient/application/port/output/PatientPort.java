package net.pladema.patient.application.port.output;

import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherPatientBo;

public interface PatientPort {

	MedicationRequestValidationDispatcherPatientBo getPatientDataNeededForMedicationRequestValidation(Integer patientId);

}
