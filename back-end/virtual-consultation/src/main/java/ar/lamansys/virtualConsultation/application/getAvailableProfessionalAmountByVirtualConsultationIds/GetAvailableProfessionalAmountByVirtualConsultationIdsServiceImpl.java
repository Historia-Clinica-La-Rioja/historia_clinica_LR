package ar.lamansys.virtualConsultation.application.getAvailableProfessionalAmountByVirtualConsultationIds;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationAvailableProfessionalAmountBo;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.VVirtualConsultationProfessionalAmountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class GetAvailableProfessionalAmountByVirtualConsultationIdsServiceImpl implements GetAvailableProfessionalAmountByVirtualConsultationIdsService {

	private final VVirtualConsultationProfessionalAmountRepository vVirtualConsultationProfessionalAmountRepository;

	@Override
	public List<VirtualConsultationAvailableProfessionalAmountBo> run(List<Integer> virtualConsultationIds) {
		log.debug("Input parameters -> virtualConsultationIds {}", virtualConsultationIds);
		List<VirtualConsultationAvailableProfessionalAmountBo> result = vVirtualConsultationProfessionalAmountRepository.getAvailableProfessionalAmountByVirtualConsultationIds(virtualConsultationIds);
		log.debug("Output -> {}", result);
		return result;
	}

}
