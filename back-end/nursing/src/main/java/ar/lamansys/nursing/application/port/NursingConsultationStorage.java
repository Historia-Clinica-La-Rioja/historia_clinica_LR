package ar.lamansys.nursing.application.port;

import ar.lamansys.nursing.domain.NursingConsultationInfoBo;
import ar.lamansys.nursing.infrastructure.output.repository.NursingConsultation;

import java.util.List;

public interface NursingConsultationStorage {

    Integer save(NursingConsultationInfoBo nursingConsultationInfoBo);
	List<Integer> getNursingConsultationIdsFromPatients(List<Integer> patients);
	List<NursingConsultation> findAllByIds(List<Integer> ids);

}
