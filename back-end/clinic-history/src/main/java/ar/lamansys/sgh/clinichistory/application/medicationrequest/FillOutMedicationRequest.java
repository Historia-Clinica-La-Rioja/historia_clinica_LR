package ar.lamansys.sgh.clinichistory.application.medicationrequest;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.ports.ClinicHistoryMedicationRequestPort;
import ar.lamansys.sgh.clinichistory.domain.document.impl.MedicationRequestBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FillOutMedicationRequest {

	private final DocumentService documentService;

	private final ClinicHistoryMedicationRequestPort clinicHistoryMedicationRequestPort;

	public void run(MedicationRequestBo medicationRequest) {
		log.debug("Input parameters -> medicationRequest {}", medicationRequest);
		medicationRequest.setMedications(documentService.getMedicationStateFromDocument(medicationRequest.getId()));
		medicationRequest.setUuid(clinicHistoryMedicationRequestPort.fetchMedicationRequestUUIDById(medicationRequest.getEncounterId()));
	}

}
