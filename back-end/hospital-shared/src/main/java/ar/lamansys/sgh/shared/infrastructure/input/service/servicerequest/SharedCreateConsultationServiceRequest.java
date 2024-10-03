package ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest;

import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.shared.domain.servicerequest.SharedAddObservationsCommandVo;

import org.springframework.web.multipart.MultipartFile;


public interface SharedCreateConsultationServiceRequest {

	Integer createOutpatientServiceRequest(Integer doctorId, String categoryId, Integer institutionId, String healthConditionSctid, String healthConditionPt, Integer medicalCoverageId, Integer outpatientConsultationId, String snomedSctid, String snomedPt, Boolean createAsFinal, Optional<SharedAddObservationsCommandVo> addObservationsCommand, Integer patientId, Short patientGenderId, Short patientAge, List<MultipartFile> files, String textObservation);

	Integer createOdontologyServiceRequest(Integer doctorId, String categoryId, Integer institutionId, String healthConditionSctid, String healthConditionPt, Integer medicalCoverageId, Integer outpatientConsultationId, String snomedSctid, String snomedPt, Boolean createAsFinal, Optional<SharedAddObservationsCommandVo> addObservationsCommand, Integer patientId, Short patientGenderId, Short patientAge, List<MultipartFile> files, String textObservation);

	Integer createNursingServiceRequest(Integer doctorId, String categoryId, Integer institutionId, String healthConditionSctid, String healthConditionPt, Integer medicalCoverageId, Integer outpatientConsultationId, String snomedSctid, String snomedPt, Boolean createAsFinal, Optional<SharedAddObservationsCommandVo> addObservationsCommand, Integer patientId, Short patientGenderId, Short patientAge);

}
