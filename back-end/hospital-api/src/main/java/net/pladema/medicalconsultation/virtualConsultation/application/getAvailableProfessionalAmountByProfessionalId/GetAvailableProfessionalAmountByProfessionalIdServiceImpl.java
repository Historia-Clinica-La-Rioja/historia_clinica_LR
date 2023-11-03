package net.pladema.medicalconsultation.virtualConsultation.application.getAvailableProfessionalAmountByProfessionalId;

import java.util.List;

import org.springframework.stereotype.Service;

import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationAvailableProfessionalAmountBo;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.repository.VVirtualConsultationProfessionalAmountRepository;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.repository.VirtualConsultationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class GetAvailableProfessionalAmountByProfessionalIdServiceImpl implements GetAvailableProfessionalAmountByProfessionalIdService {

	private final VirtualConsultationRepository virtualConsultationRepository;

	private final VVirtualConsultationProfessionalAmountRepository vVirtualConsultationProfessionalAmountRepository;

	@Override
	public List<VirtualConsultationAvailableProfessionalAmountBo> run(Integer healthcareProfessionalId) {
		log.debug("Input parameters -> healthcareProfessionalId {}", healthcareProfessionalId);
		List<Integer> virtualConsultationIds = virtualConsultationRepository.getVirtualConsultationIdsByPotentialHealthcareProfessionalId(healthcareProfessionalId);
		List<VirtualConsultationAvailableProfessionalAmountBo> result = vVirtualConsultationProfessionalAmountRepository.getAvailableProfessionalAmountByVirtualConsultationIds(virtualConsultationIds);
		log.debug("Output -> {}", result);
		return result;
	}

}
