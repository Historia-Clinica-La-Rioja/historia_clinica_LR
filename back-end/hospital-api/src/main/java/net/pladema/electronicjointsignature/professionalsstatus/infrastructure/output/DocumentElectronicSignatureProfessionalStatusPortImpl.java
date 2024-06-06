package net.pladema.electronicjointsignature.professionalsstatus.infrastructure.output;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentInvolvedProfessionalRepository;
import lombok.AllArgsConstructor;
import net.pladema.electronicjointsignature.professionalsstatus.application.port.DocumentElectronicSignatureProfessionalStatusPort;

import net.pladema.electronicjointsignature.professionalsstatus.domain.DocumentElectronicSignatureProfessionalStatusBo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@AllArgsConstructor
@Service
public class DocumentElectronicSignatureProfessionalStatusPortImpl implements DocumentElectronicSignatureProfessionalStatusPort {

	private DocumentElectronicSignatureProfessionalStatusStorage documentElectronicSignatureProfessionalStatusStorage;

	private DocumentInvolvedProfessionalRepository documentInvolvedProfessionalRepository;

	@Override
	public List<DocumentElectronicSignatureProfessionalStatusBo> fetchDocumentInvolvedProfessionalStatus(Long documentId) {
		List<DocumentElectronicSignatureProfessionalStatusBo> result = documentElectronicSignatureProfessionalStatusStorage.fetch(documentId);
		result.forEach(this::checkAndUpdateOutdatedStatus);
		return result;
	}

	private void checkAndUpdateOutdatedStatus(DocumentElectronicSignatureProfessionalStatusBo professionalStatus) {
		LocalDate currentDate = LocalDate.now();
		if (professionalStatus.getStatusId().equals(EElectronicSignatureStatus.PENDING.getId()) && ChronoUnit.DAYS.between(professionalStatus.getDate(), currentDate) > 5)
			updateAndSetOutdatedStatusId(professionalStatus, currentDate);
	}

	private void updateAndSetOutdatedStatusId(DocumentElectronicSignatureProfessionalStatusBo professionalStatus, LocalDate currentDate) {
		documentInvolvedProfessionalRepository.updateSignatureStatus(professionalStatus.getDocumentInvolvedProfessionalId(), EElectronicSignatureStatus.OUTDATED.getId(), currentDate);
		professionalStatus.setStatusId(EElectronicSignatureStatus.OUTDATED.getId());
	}

}
