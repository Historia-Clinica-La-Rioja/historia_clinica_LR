package ar.lamansys.sgh.shared.infrastructure.input.service.nursing;

import java.util.List;

public interface SharedNursingConsultationPort {

	List<Integer> getNursingConsultationIdsFromPatients(List<Integer> patients);
	List<NursingConsultationInfoDto> findAllById(List<Integer> ids);

}
