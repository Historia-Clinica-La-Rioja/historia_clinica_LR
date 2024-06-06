package net.pladema.electronicjointsignature.documentlist.infrastructure.output;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentInvolvedProfessionalRepository;
import lombok.AllArgsConstructor;
import net.pladema.electronicjointsignature.documentlist.application.port.GetProfessionalInvolvedDocumentListPort;

import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureDocumentListFilterBo;
import net.pladema.electronicjointsignature.documentlist.domain.ElectronicSignatureInvolvedDocumentBo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Service
public class GetProfessionalInvolvedDocumentListPortImpl implements GetProfessionalInvolvedDocumentListPort {

	private GetProfessionalInvolvedDocumentListStorage getProfessionalInvolvedDocumentListStorage;

	private DocumentInvolvedProfessionalRepository documentInvolvedProfessionalRepository;

	@Override
	public Page<ElectronicSignatureInvolvedDocumentBo> fetchProfessionalInvolvedDocuments(ElectronicSignatureDocumentListFilterBo filter, Pageable pageable) {
		Page<ElectronicSignatureInvolvedDocumentBo> result = getProfessionalInvolvedDocumentListStorage.run(filter, pageable);
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
