package ar.lamansys.sgh.clinichistory.application.medicationrequest;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.ports.ClinicHistoryMedicationRequestPort;
import ar.lamansys.sgh.clinichistory.application.ports.output.DocumentMedicationStatementPort;
import ar.lamansys.sgh.clinichistory.domain.GetCommercialStatementCommercialPrescriptionBo;
import ar.lamansys.sgh.clinichistory.domain.document.impl.MedicationRequestBo;
import ar.lamansys.sgh.clinichistory.domain.ips.CommercialMedicationPrescriptionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FillOutMedicationRequest {

	private final DocumentService documentService;

	private final ClinicHistoryMedicationRequestPort clinicHistoryMedicationRequestPort;

	private final DocumentMedicationStatementPort documentMedicationStatementPort;

	public void run(MedicationRequestBo medicationRequest) {
		log.debug("Input parameters -> medicationRequest {}", medicationRequest);
		medicationRequest.setMedications(getMedicationsFromDocument(medicationRequest));
		medicationRequest.setUuid(clinicHistoryMedicationRequestPort.fetchMedicationRequestUUIDById(medicationRequest.getEncounterId()));
	}

	private List<MedicationBo> getMedicationsFromDocument(MedicationRequestBo medicationRequest) {
		List<MedicationBo> result = documentService.getMedicationStateFromDocument(medicationRequest.getId());
		Map<Integer, GetCommercialStatementCommercialPrescriptionBo> commercialPrescriptions = documentMedicationStatementPort.getCommercialStatementCommercialPrescriptionByDocumentId(medicationRequest.getId()).stream()
				.collect(Collectors.toMap(GetCommercialStatementCommercialPrescriptionBo::getMedicationStatementId, medication -> medication));
		result.forEach(medication -> setCommercialMedicationPrescriptionData(medication, commercialPrescriptions));
		return result;
	}

	private void setCommercialMedicationPrescriptionData(MedicationBo medication, Map<Integer, GetCommercialStatementCommercialPrescriptionBo> commercialPrescriptions) {
		GetCommercialStatementCommercialPrescriptionBo commercialPrescription = commercialPrescriptions.get(medication.getId());
		if (commercialPrescription != null)
			medication.setCommercialMedicationPrescription(CommercialMedicationPrescriptionBo.builder()
					.medicationPackQuantity(commercialPrescription.getMedicationPackQuantity())
					.presentationUnitQuantity(commercialPrescription.getPresentationUnitQuantity())
					.build());
	}

}
