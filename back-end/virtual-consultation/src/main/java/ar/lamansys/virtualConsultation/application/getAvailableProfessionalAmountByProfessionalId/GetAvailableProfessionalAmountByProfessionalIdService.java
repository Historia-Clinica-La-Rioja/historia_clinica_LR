package ar.lamansys.virtualConsultation.application.getAvailableProfessionalAmountByProfessionalId;

import java.util.List;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationAvailableProfessionalAmountBo;

public interface GetAvailableProfessionalAmountByProfessionalIdService {

	List<VirtualConsultationAvailableProfessionalAmountBo> run(Integer healthcareProfessionalId);

}
