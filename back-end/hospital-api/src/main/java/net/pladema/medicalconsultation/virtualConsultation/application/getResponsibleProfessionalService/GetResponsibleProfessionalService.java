package net.pladema.medicalconsultation.virtualConsultation.application.getResponsibleProfessionalService;

import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationResponsibleDataDto;

public interface GetResponsibleProfessionalService {
	VirtualConsultationResponsibleDataDto run (Integer institution, Integer responsibleHealthcareProfessionalId);
}