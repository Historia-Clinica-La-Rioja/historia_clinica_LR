package net.pladema.medicalconsultation.virtualConsultation.application.getVirtualConsultationsByInstitution;

import java.util.List;

import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationBo;
import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationFilterBo;

public interface GetVirtualConsultationsByInstitutionService {

	List<VirtualConsultationBo> run(VirtualConsultationFilterBo filter);

}
