package ar.lamansys.sgh.publicapi.patient.application.fetchprescriptionsdatabydni;

import ar.lamansys.sgh.publicapi.patient.application.fetchprescriptionsdatabydni.exception.PatientPrescriptionsAccessDeniedException;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.service.PatientInformationPublicApiPermission;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionStorage;

import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionsDataBo;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class FetchPrescriptionsDataByDni {

	private final PrescriptionStorage prescriptionStorage;
	private final PatientInformationPublicApiPermission patientInformationPublicApiPermission;

	public List<PrescriptionsDataBo> run(String identificationNumber) {
		assertUserCanAccess();
		log.debug("Input parameters ->  identificationNumber {}", identificationNumber);
		List<PrescriptionsDataBo> result = getFromStorage(identificationNumber);
		log.debug("Output -> {}", result);
		return result;
	}

	private List<PrescriptionsDataBo> getFromStorage(String identificationNumber) {
		return prescriptionStorage.getPrescriptionsDataByDni(identificationNumber).orElse(Collections.emptyList());
	}

	private void assertUserCanAccess() {
		if (!patientInformationPublicApiPermission.canAccessPrescriptionDataFromPatientIdNumber()) {
			throw new PatientPrescriptionsAccessDeniedException();
		}
	}
}
