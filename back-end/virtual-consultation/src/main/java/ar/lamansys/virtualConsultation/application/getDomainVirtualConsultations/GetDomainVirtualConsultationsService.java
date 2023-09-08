package ar.lamansys.virtualConsultation.application.getDomainVirtualConsultations;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationBo;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationFilterBo;

import java.util.List;

public interface GetDomainVirtualConsultationsService {

	List<VirtualConsultationBo> run(Integer institutionId, VirtualConsultationFilterBo filter);

}
