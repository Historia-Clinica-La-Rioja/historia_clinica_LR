package ar.lamansys.sgh.publicapi.patient.infrastructure.input.service;

public interface PatientInformationPublicApiPermission {

	boolean canAccessPersonFromIdPatient();

	boolean canAccessPrescriptionDataFromPatientIdNumber();

	boolean canAccessAppointmentsDataFromPatientIdNumber();

	boolean canAccessSaveExternalClinicHistory();

	boolean canAccessSaveExternalEncounter();

	boolean canAccessDeleteExternalEncounter();

	boolean canAccessSaveExternalPatient();

}
