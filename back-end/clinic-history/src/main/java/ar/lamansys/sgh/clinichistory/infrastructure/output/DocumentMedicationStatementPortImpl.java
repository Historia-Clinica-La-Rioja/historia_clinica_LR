package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.output.DocumentMedicationStatementPort;
import ar.lamansys.sgh.clinichistory.domain.GetCommercialStatementCommercialPrescriptionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentMedicamentionStatementRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DocumentMedicationStatementPortImpl implements DocumentMedicationStatementPort {

	private final DocumentMedicamentionStatementRepository documentMedicamentionStatementRepository;

	@Override
	public List<GetCommercialStatementCommercialPrescriptionBo> getCommercialStatementCommercialPrescriptionByDocumentId(Long documentId) {
		return documentMedicamentionStatementRepository.fetchCommercialStatementCommercialPrescriptionByDocumentId(documentId);
	}

}
