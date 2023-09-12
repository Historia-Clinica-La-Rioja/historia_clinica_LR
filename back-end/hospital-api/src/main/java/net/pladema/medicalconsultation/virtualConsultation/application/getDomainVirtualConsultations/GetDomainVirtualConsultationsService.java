package net.pladema.medicalconsultation.virtualConsultation.application.getDomainVirtualConsultations;

import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationBo;
import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationFilterBo;

import java.util.List;

public interface GetDomainVirtualConsultationsService {

	List<VirtualConsultationBo> run(Integer institutionId, VirtualConsultationFilterBo filter);

}
