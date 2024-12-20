package ar.lamansys.odontology.application.odontogram.ports;

import ar.lamansys.odontology.domain.consultation.ConsultationInfoBo;
import ar.lamansys.odontology.infrastructure.repository.consultation.OdontologyConsultation;

import java.util.List;
import java.util.Optional;

public interface OdontologyConsultationStorage {

    Integer save(ConsultationInfoBo consultationInfo);

    boolean hasPreviousConsultations(Integer patientId);

	List<Integer> getOdontologyConsultationIdsFromPatients(List<Integer> patients);
	List<OdontologyConsultation> findAllById(List<Integer> ids);

	OdontologyConsultation getLastByPatientId(Integer patientId);

	Optional<Integer> getPatientMedicalCoverageId(Integer id);

	Optional<Long> getOdontologyDocumentId(Integer healthCondition);

}
