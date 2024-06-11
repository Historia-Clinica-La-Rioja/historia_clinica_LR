package ar.lamansys.sgh.publicapi.patient.infrastructure.input.service;

public interface PatientInformationPublicApiPermission {
	boolean canAccessPersonFromIdPatient();

	boolean canAccessPrescriptionDataFromPatientIdNumber();
}
