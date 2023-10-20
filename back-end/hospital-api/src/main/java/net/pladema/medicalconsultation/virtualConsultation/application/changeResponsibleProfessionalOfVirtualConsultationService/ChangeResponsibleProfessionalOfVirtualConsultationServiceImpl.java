package net.pladema.medicalconsultation.virtualConsultation.application.changeResponsibleProfessionalOfVirtualConsultationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.repository.VirtualConsultationRepository;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class ChangeResponsibleProfessionalOfVirtualConsultationServiceImpl implements ChangeResponsibleProfessionalOfVirtualConsultationService {

	private final VirtualConsultationRepository virtualConsultationRepository;

	@Override
	public Boolean run(Integer virtualConsultationId, Integer responsibleHealthcareProfessionalId){
		log.debug("input parameters -> virtualConsultationId{}, responsibleHealthcareProfessionalId{}", virtualConsultationId, responsibleHealthcareProfessionalId);
		virtualConsultationRepository.updateResponsibleId(virtualConsultationId,responsibleHealthcareProfessionalId);
		return true;
	}
}
