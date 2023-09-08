package net.pladema.medicalconsultation.virtualConsultation.application.saveVirtualConsultation;

import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationRequestBo;

public interface SaveVirtualConsultationRequestService {

	public Integer run(VirtualConsultationRequestBo virtualConsultation);

}
