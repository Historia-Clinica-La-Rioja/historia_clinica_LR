package ar.lamansys.virtualConsultation.application.getVirtualConsultationsByInstitution;

import java.util.List;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationBo;

public interface GetVirtualConsultationsByInstitutionService {

	List<VirtualConsultationBo> run(Integer institutionId);

}
