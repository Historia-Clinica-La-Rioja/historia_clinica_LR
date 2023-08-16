package ar.lamansys.virtualConsultation.application.getIdsByPosibleHealthcareProfessionalId;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.virtualConsultation.infrastructure.output.repository.VirtualConsultationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class GetVirtualConsultationIdsByPotentialHealthcareProfessionalIdServiceImpl implements GetVirtualConsultationIdsByPotentialHealthcareProfessionalIdService {

	private final VirtualConsultationRepository virtualConsultationRepository;

	@Override
	public List<Integer> run(Integer healthcareProfessionalId) {
		log.debug("Input parameters -> healthcareProfessionalId {}", virtualConsultationRepository);
		List<Integer> result = virtualConsultationRepository.getVirtualConsultationIdsByPotentialHealthcareProfessionalId(healthcareProfessionalId);
		log.debug("Output -> {}", result);
		return result;
	}

}
