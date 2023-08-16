package ar.lamansys.virtualConsultation.application.getAvailableProfessionalAmountByVirtualConsultationIds;

import java.util.List;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationAvailableProfessionalAmountBo;

public interface GetAvailableProfessionalAmountByVirtualConsultationIdsService {

	List<VirtualConsultationAvailableProfessionalAmountBo> run(List<Integer> virtualConsultationIds);

}
