package ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest;

import java.util.Optional;

import ar.lamansys.sgh.shared.domain.servicerequest.SharedAddObservationsCommandVo;

import org.springframework.transaction.annotation.Transactional;


public interface SharedCreateConsultationServiceRequest {

	Integer createOutpatientServiceRequest(Integer doctorId, String categoryId, Integer institutionId, String healthConditionSctid, String healthConditionPt, Integer medicalCoverageId, Integer outpatientConsultationId, String snomedSctid, String snomedPt, Boolean createAsFinal, Optional<SharedAddObservationsCommandVo> addObservationsCommand, Integer patientId, Short patientGenderId, Short patientAge);

	Integer createOdontologyServiceRequest(Integer doctorId, String categoryId, Integer institutionId, String healthConditionSctid, String healthConditionPt, Integer medicalCoverageId, Integer outpatientConsultationId, String snomedSctid, String snomedPt, Boolean createAsFinal, Optional<SharedAddObservationsCommandVo> addObservationsCommand, Integer patientId, Short patientGenderId, Short patientAge);

	Integer createNursingServiceRequest(Integer doctorId, String categoryId, Integer institutionId, String healthConditionSctid, String healthConditionPt, Integer medicalCoverageId, Integer outpatientConsultationId, String snomedSctid, String snomedPt, Boolean createAsFinal, Optional<SharedAddObservationsCommandVo> addObservationsCommand, Integer patientId, Short patientGenderId, Short patientAge);
}
