package ar.lamansys.odontology.domain.consultation;

import ar.lamansys.odontology.infrastructure.repository.consultation.OdontologyConsultation;

import java.util.List;

public interface OdontologyConsultationStorage {

    Integer save(ConsultationInfoBo consultationInfo);

    boolean hasPreviousConsultations(Integer patientId);

	List<Integer> getOdontologyConsultationIdsFromPatients(List<Integer> patients);
	List<OdontologyConsultation> findAllById(List<Integer> ids);

	OdontologyConsultation getLastByPatientId(Integer patientId);

}
