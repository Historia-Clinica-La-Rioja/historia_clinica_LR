package ar.lamansys.virtualConsultation.application.getVirtualConsultationsByInstitution;

import java.util.List;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationBo;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationFilterBo;

public interface GetVirtualConsultationsByInstitutionService {

	List<VirtualConsultationBo> run(VirtualConsultationFilterBo filter);

}
