package ar.lamansys.immunization.domain.consultation;

import ar.lamansys.immunization.infrastructure.output.repository.consultation.VaccineConsultation;

import java.util.List;

public interface VaccineConsultationStorage {

    Integer save(VaccineConsultationBo vaccineConsultationBo);
	List<Integer> getVaccineConsultationIdsFromPatients(List<Integer> patients);
	List<VaccineConsultation> findAllByIds(List<Integer> ids);
}
