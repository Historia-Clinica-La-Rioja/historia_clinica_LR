package net.pladema.medicalconsultation.virtualConsultation.application.getVirtualConsultationNotificationData;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationNotificationDataBo;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.repository.VirtualConsultationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class GetVirtualConsultationNotificationDataServiceImpl implements GetVirtualConsultationNotificationDataService {

	private final VirtualConsultationRepository virtualConsultationRepository;

	private final FeatureFlagsService featureFlagsService;

	@Override
	public VirtualConsultationNotificationDataBo run(Integer virtualConsultationId) {
		log.debug("Input parameters ->  virtualConsultationId {}", virtualConsultationId);
		VirtualConsultationNotificationDataBo result = virtualConsultationRepository.getVirtualConsultationNotificationData(virtualConsultationId);
		if (featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && result.getPatientSelfPerceivedName() != null)
			result.setPatientName(result.getPatientSelfPerceivedName());
		log.debug("Output -> {}", result);
		return result;
	}

}
