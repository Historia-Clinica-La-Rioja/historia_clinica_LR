package ar.lamansys.virtualConsultation.infrastructure.output.repository;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationBo;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationFilterBo;

import java.util.List;

public interface ListVirtualConsultationRepository {

	List<VirtualConsultationBo> getDomainVirtualConsultation(List<Integer> clinicalSpecialties, List<Integer> careLines, VirtualConsultationFilterBo filter);

	List<VirtualConsultationBo> getInstitutionVirtualConsultation(VirtualConsultationFilterBo filter);

}
