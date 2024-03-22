package net.pladema.electronicjointsignature.signdocument.infrastructure.output;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentInvolvedProfessionalRepository;
import lombok.AllArgsConstructor;
import net.pladema.electronicjointsignature.signdocument.application.port.SignDocumentElectronicJointSignaturePort;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class SignDocumentElectronicJointSignaturePortImpl implements SignDocumentElectronicJointSignaturePort {

	private DocumentInvolvedProfessionalRepository documentInvolvedProfessionalRepository;

	@Override
	public void changeDocumentSignatureStatusToSigned(Integer healthcareProfessionalId, List<Long> documentIds) {
		LocalDate currentDate = LocalDate.now();
		documentInvolvedProfessionalRepository.updateSignatureStatusByDocumentAndHealthcareProfessionalId(documentIds, healthcareProfessionalId, EElectronicSignatureStatus.SIGNED.getId(), currentDate);
	}

	@Override
	public List<Short> fetchDocumentsSignatureStatus(Integer healthcareProfessionalId, List<Long> documentIds) {
		return documentInvolvedProfessionalRepository.getSignatureStatusIdByDocumentAndHealthcareProfessionalIds(healthcareProfessionalId, documentIds);
	}

}
