package ar.lamansys.sgh.publicapi.patient.application.fetchappointmentsdatabydni;

import ar.lamansys.sgh.publicapi.patient.application.fetchpatientpersonbyid.exception.PatientPersonAccessDeniedException;

import ar.lamansys.sgh.publicapi.patient.infrastructure.input.service.PatientInformationPublicApiPermission;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.patient.application.port.out.AppointmentStorage;
import ar.lamansys.sgh.publicapi.patient.domain.AppointmentsByUserBo;

@Slf4j
@AllArgsConstructor
@Service
public class FetchAppointmentsDataByDni {

	private final AppointmentStorage appointmentStorage;
	private final PatientInformationPublicApiPermission patientInformationPublicApiPermission;

	public AppointmentsByUserBo run(String identificationNumber, Short identificationTypeId, Short genderId, String birthDate) {
		assertUserCanAccess();
		log.debug("Input parameters ->  identificationNumber {}", identificationNumber);
		AppointmentsByUserBo result = getFromStorage(identificationNumber, identificationTypeId, genderId, birthDate);
		log.debug("Output -> {}", result);
		return result;
	}

	private AppointmentsByUserBo getFromStorage(String identificationNumber, Short identificationTypeId, Short genderId, String birthDate) {
		return appointmentStorage.getAppointmentsDataByDni(identificationNumber, identificationTypeId, genderId, birthDate);
	}

	private void assertUserCanAccess() {
		if (!patientInformationPublicApiPermission.canAccessAppointmentsDataFromPatientIdNumber()) {
			throw new PatientPersonAccessDeniedException();
		}
	}
}