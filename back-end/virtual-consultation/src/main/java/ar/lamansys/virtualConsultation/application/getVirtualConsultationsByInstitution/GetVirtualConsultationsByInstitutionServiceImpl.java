package ar.lamansys.virtualConsultation.application.getVirtualConsultationsByInstitution;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationBo;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.VirtualConsultationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class GetVirtualConsultationsByInstitutionServiceImpl implements GetVirtualConsultationsByInstitutionService {

	private final VirtualConsultationRepository virtualConsultationRepository;

	@Override
	public List<VirtualConsultationBo> run(Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<VirtualConsultationBo> result = virtualConsultationRepository.getVirtualConsultationsByInstitutionId(institutionId);
		log.debug("Output -> {}", result);
		return result;
	}

}
