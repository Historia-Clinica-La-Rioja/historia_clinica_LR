package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.medicationrequests.repository.MedicationRequestRepository;
import net.pladema.clinichistory.requests.medicationrequests.service.GetMedicationRequestByDocument;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetMedicationRequestByDocumentImpl implements GetMedicationRequestByDocument {

	private final MedicationRequestRepository repository;


	@Override
	public Integer run(Long documentId) {
		log.debug("Input parameter -> documentId {}", documentId);
		Integer result = repository.getIdByDocumentId(documentId).orElseThrow(() -> new NotFoundException("medication-request-not-found", "No se encontro la receta asociada al documento"));
		log.debug("Output -> {}", result);
		return result;
	}
}
