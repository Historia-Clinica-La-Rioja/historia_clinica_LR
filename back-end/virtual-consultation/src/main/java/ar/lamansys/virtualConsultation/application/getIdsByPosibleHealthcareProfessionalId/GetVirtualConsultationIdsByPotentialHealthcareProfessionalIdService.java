package ar.lamansys.virtualConsultation.application.getIdsByPosibleHealthcareProfessionalId;

import java.util.List;

public interface GetVirtualConsultationIdsByPotentialHealthcareProfessionalIdService {

	List<Integer> run(Integer healthcareProfessionalId);

}
