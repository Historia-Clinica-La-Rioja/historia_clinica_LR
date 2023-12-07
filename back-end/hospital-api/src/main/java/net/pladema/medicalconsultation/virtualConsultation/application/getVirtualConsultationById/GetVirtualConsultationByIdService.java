package net.pladema.medicalconsultation.virtualConsultation.application.getVirtualConsultationById;

import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationBo;

public interface GetVirtualConsultationByIdService {

	VirtualConsultationBo run(Integer virtualConsultationId);

}
