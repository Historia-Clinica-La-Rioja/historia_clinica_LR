package ar.lamansys.virtualConsultation.application.getVirtualConsultationById;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationBo;

public interface GetVirtualConsultationByIdService {

	VirtualConsultationBo run(Integer virtualConsultationId);

}
