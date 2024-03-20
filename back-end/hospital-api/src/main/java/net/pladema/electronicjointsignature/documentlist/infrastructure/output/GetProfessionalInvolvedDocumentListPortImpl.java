package net.pladema.electronicjointsignature.documentlist.infrastructure.output;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentInvolvedProfessionalRepository;
import lombok.AllArgsConstructor;
import net.pladema.electronicjointsignature.documentlist.application.port.GetProfessionalInvolvedDocumentListPort;

import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureInvolvedDocumentBo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@AllArgsConstructor
@Service
public class GetProfessionalInvolvedDocumentListPortImpl implements GetProfessionalInvolvedDocumentListPort {

	private GetProfessionalInvolvedDocumentListStorage getProfessionalInvolvedDocumentListStorage;

	private DocumentInvolvedProfessionalRepository documentInvolvedProfessionalRepository;

	@Override
	public List<ElectronicSignatureInvolvedDocumentBo> fetchProfessionalInvolvedDocuments(Integer institutionId, Integer healthcareProfessionalId) {
		List<ElectronicSignatureInvolvedDocumentBo> result = getProfessionalInvolvedDocumentListStorage.run(institutionId, healthcareProfessionalId);
		result.forEach(this::checkAndUpdateOutdatedSignatureStatus);
		return result;
	}

	private void checkAndUpdateOutdatedSignatureStatus(ElectronicSignatureInvolvedDocumentBo document) {
		LocalDate currentDate = LocalDate.now();
		if (document.getSignatureStatusId().equals(EElectronicSignatureStatus.PENDING.getId()) && ChronoUnit.DAYS.between(document.getStatusDate(), currentDate) > 5)
			updateAndSetOutdatedStatusId(document, currentDate);
	}

	private void updateAndSetOutdatedStatusId(ElectronicSignatureInvolvedDocumentBo document, LocalDate currentDate) {
		documentInvolvedProfessionalRepository.updateSignatureStatus(document.getDocumentInvolvedProfessionalId(), EElectronicSignatureStatus.OUTDATED.getId(), currentDate);
		document.setSignatureStatusId(EElectronicSignatureStatus.OUTDATED.getId());
	}

}
