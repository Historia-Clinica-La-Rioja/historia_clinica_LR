package ar.lamansys.virtualConsultation.application.getDomainVirtualConsultations;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationBo;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.VirtualConsultationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class GetDomainVirtualConsultationsServiceImpl implements GetDomainVirtualConsultationsService {

	private final FeatureFlagsService featureFlagsService;

	private final VirtualConsultationRepository virtualConsultationRepository;

	@Override
	public List<VirtualConsultationBo> run() {
		List<VirtualConsultationBo> result = virtualConsultationRepository.getDomainVirtualConsultations();
		if (featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS))
			result.forEach(virtualConsultation -> {
				if (virtualConsultation.getPatientSelfPerceivedName() != null)
					virtualConsultation.setPatientName(virtualConsultation.getPatientSelfPerceivedName());
			});
		log.debug("Output -> {}", result);
		return result;
	}

}
