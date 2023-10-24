package net.pladema.medicalconsultation.virtualConsultation.application.getResponsibleProfessionalService;

import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.virtualConsultation.application.getResponsibleProfesionalAvailability.GetResponsibleProfessionalAvailabilityService;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationResponsibleDataDto;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class GetResponsibleProfessionalServiceImpl implements GetResponsibleProfessionalService {
	private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;
	private final GetResponsibleProfessionalAvailabilityService getResponsibleProfessionalAvailabilityService;
	@Override
	public VirtualConsultationResponsibleDataDto run(Integer institutionId, Integer responsibleHealthcareProfessionalId) {
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(responsibleHealthcareProfessionalId);
		ProfessionalCompleteDto completeData = healthcareProfessionalExternalService.getProfessionalCompleteInfoById(responsibleHealthcareProfessionalId);
		Boolean availability = getResponsibleProfessionalAvailabilityService.run(responsibleHealthcareProfessionalId, institutionId);
		VirtualConsultationResponsibleDataDto result = new VirtualConsultationResponsibleDataDto(completeData.getFirstName(), completeData.getLastName(), completeData.getId(), availability);
		return result;
	}
}
