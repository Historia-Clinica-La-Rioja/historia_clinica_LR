package ar.lamansys.sgh.shared.infrastructure.input.service.nursing;

import java.util.List;
import java.util.Optional;

public interface SharedNursingConsultationPort {

	List<Integer> getNursingConsultationIdsFromPatients(List<Integer> patients);
	List<NursingConsultationInfoDto> findAllById(List<Integer> ids);
	Optional<Integer> getPatientMedicalCoverageId(Integer id);

}
