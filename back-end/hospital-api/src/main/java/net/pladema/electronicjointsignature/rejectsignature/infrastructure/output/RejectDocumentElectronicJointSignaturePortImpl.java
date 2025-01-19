package net.pladema.electronicjointsignature.rejectsignature.infrastructure.output;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentElectronicSignatureRejectReasonRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentInvolvedProfessionalRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentElectronicSignatureRejectReason;
import lombok.AllArgsConstructor;
import net.pladema.electronicjointsignature.rejectsignature.application.port.RejectDocumentElectronicJointSignaturePort;

import net.pladema.electronicjointsignature.rejectsignature.domain.RejectDocumentElectronicJointSignatureBo;

import net.pladema.electronicjointsignature.rejectsignature.domain.RejectDocumentElectronicJointSignatureReasonBo;
import net.pladema.electronicjointsignature.rejectsignature.domain.RejectElectronicJointSignatureDocumentInvolvedProfessionalBo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RejectDocumentElectronicJointSignaturePortImpl implements RejectDocumentElectronicJointSignaturePort {

	private RejectDocumentElectronicJointSignatureStorage rejectDocumentElectronicJointSignatureStorage;

	private DocumentInvolvedProfessionalRepository documentInvolvedProfessionalRepository;

	private DocumentElectronicSignatureRejectReasonRepository documentElectronicSignatureRejectReasonRepository;

	@Override
	public Optional<RejectElectronicJointSignatureDocumentInvolvedProfessionalBo> getDocumentInvolvedProfessionalByProfessionalAndDocumentId(Long documentId, Integer healthcareProfessionalId) {
		Optional<RejectElectronicJointSignatureDocumentInvolvedProfessionalBo> result = rejectDocumentElectronicJointSignatureStorage.fetchSignatureData(documentId, healthcareProfessionalId);
        result.ifPresent(this::checkAndUpdateOutdatedSignatureStatus);
		return result;
	}

	private void checkAndUpdateOutdatedSignatureStatus(RejectElectronicJointSignatureDocumentInvolvedProfessionalBo rejectData) {
		LocalDate currentDate = LocalDate.now();
		final int SIGNATURE_MAX_VALID_DAY_AMOUNT = 5;
		if (rejectData.getStatusId().equals(EElectronicSignatureStatus.PENDING.getId()) && ChronoUnit.DAYS.between(rejectData.getStatusUpdateDate(), currentDate) > SIGNATURE_MAX_VALID_DAY_AMOUNT)
			updateAndSetOutdatedStatusId(rejectData, currentDate);
	}

	private void updateAndSetOutdatedStatusId(RejectElectronicJointSignatureDocumentInvolvedProfessionalBo rejectData, LocalDate currentDate) {
		documentInvolvedProfessionalRepository.updateSignatureStatus(rejectData.getDocumentInvolvedProfessionalId(), EElectronicSignatureStatus.OUTDATED.getId(), currentDate);
		rejectData.setStatusId(EElectronicSignatureStatus.OUTDATED.getId());
	}

	@Override
	@Transactional
	public Integer saveRejectData(Integer documentInvolvedProfessionalId, RejectDocumentElectronicJointSignatureBo rejectReason) {
		documentInvolvedProfessionalRepository.updateSignatureStatus(documentInvolvedProfessionalId, EElectronicSignatureStatus.REJECTED.getId(), LocalDate.now());
		documentElectronicSignatureRejectReasonRepository.save(parseToDocumentElectronicSignatureRejectReason(documentInvolvedProfessionalId, rejectReason.getReason()));
		return documentInvolvedProfessionalId;
	}

	private DocumentElectronicSignatureRejectReason parseToDocumentElectronicSignatureRejectReason(Integer involvedProfessionalSignatureId, RejectDocumentElectronicJointSignatureReasonBo reason) {
		DocumentElectronicSignatureRejectReason result = new DocumentElectronicSignatureRejectReason();
		result.setDocumentInvolvedProfessionalId(involvedProfessionalSignatureId);
		result.setDescription(reason.getDescription());
		result.setReasonId(reason.getRejectReasonId());
		return result;
	}

}
