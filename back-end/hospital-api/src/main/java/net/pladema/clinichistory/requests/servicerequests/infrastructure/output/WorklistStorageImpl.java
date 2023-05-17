package net.pladema.clinichistory.requests.servicerequests.infrastructure.output;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.application.port.WorklistStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.WorklistBo;

import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.service.EInformerWorklistStatus;

import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;

import net.pladema.permissions.repository.enums.ERole;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorklistStorageImpl implements WorklistStorage {

	private final FeatureFlagsService featureFlagsService;
	private final AppointmentRepository appointmentRepository;

	@Override
	public List<WorklistBo> getWorklistByModalityAndInstitution(Integer modalityId, Integer institutionId) {
		log.debug("Get worklist by modalityId {}, institutionId {}", modalityId, institutionId);

		List<WorklistBo> result = appointmentRepository.getPendingWorklistByModalityAndInstitution(modalityId, institutionId, ERole.TECNICO.getId()).stream().map(w -> {
			w.setPatientFullName(w.getFullName(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS)));
			w.setStatusId(EInformerWorklistStatus.PENDING.getId());
			return w;
		}).collect(Collectors.toList());

		result.addAll(appointmentRepository.getCompletedWorklistByModalityAndInstitution(modalityId, institutionId).stream().map( w-> {
			w.setPatientFullName(w.getFullName(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS)));
			w.setStatusId(EInformerWorklistStatus.COMPLETED.getId());
			return w;
		}).collect(Collectors.toList()));

		result.sort(Comparator.comparing(WorklistBo::getActionTime));

		return result;
	}

}
