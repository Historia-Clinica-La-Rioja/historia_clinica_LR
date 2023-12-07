package net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.repository;

import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationBo;
import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationFilterBo;

import java.util.List;

public interface ListVirtualConsultationRepository {

	List<VirtualConsultationBo> getDomainVirtualConsultation(List<Integer> clinicalSpecialties, List<Integer> careLines, Integer healthcareProfessionalId, VirtualConsultationFilterBo filter);

	List<VirtualConsultationBo> getInstitutionVirtualConsultation(VirtualConsultationFilterBo filter);

}
