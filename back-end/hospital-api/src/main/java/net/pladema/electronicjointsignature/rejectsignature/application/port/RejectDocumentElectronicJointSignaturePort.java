package net.pladema.electronicjointsignature.rejectsignature.application.port;

import net.pladema.electronicjointsignature.rejectsignature.domain.RejectDocumentElectronicJointSignatureBo;
import net.pladema.electronicjointsignature.rejectsignature.domain.RejectElectronicJointSignatureDocumentInvolvedProfessionalBo;

import java.util.Optional;

public interface RejectDocumentElectronicJointSignaturePort {

	Optional<RejectElectronicJointSignatureDocumentInvolvedProfessionalBo> getDocumentInvolvedProfessionalByProfessionalAndDocumentId(Long documentId, Integer healthcareProfessionalId);

	Integer saveRejectData(Integer documentInvolvedProfessionalId, RejectDocumentElectronicJointSignatureBo rejectData);

}