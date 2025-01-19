package ar.lamansys.sgh.clinichistory.application.medicationrequest;

import ar.lamansys.sgh.clinichistory.domain.document.impl.MedicationRequestBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadMedications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaveMedicationRequestComponents {

	private final LoadMedications loadMedications;

	public void run(MedicationRequestBo medicationRequest) {
		log.debug("Input parameters -> medicationRequest {}", medicationRequest);
		loadMedications.run(medicationRequest.getPatientId(), medicationRequest.getId(), medicationRequest.getMedications());
	}

}
