package ar.lamansys.virtualConsultation.application.getDomainVirtualConsultations;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationBo;

import java.util.List;

public interface GetDomainVirtualConsultationsService {

	List<VirtualConsultationBo> run(Integer institutionId);

}
