package ar.lamansys.virtualConsultation.application.getVirtualConsultationById;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationBo;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.VirtualConsultationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class GetVirtualConsultationByIdServiceImpl implements GetVirtualConsultationByIdService {

	private final VirtualConsultationRepository virtualConsultationRepository;

	private final FeatureFlagsService featureFlagsService;

	@Override
	public VirtualConsultationBo run(Integer virtualConsultationId) {
		log.debug("Input parameters -> virtualConsultationId {}", virtualConsultationId);
		VirtualConsultationBo result = virtualConsultationRepository.getVirtualConsultationById(virtualConsultationId);
		if (featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS))
			result.setPatientName(result.getPatientSelfPerceivedName());
		log.debug("Output -> {}", result);
		return result;
	}

}
