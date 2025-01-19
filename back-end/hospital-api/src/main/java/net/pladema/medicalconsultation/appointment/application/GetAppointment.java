package net.pladema.medicalconsultation.appointment.application;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.appointment.application.port.AppointmentPort;

import net.pladema.medicalconsultation.appointment.application.port.AppointmentReferenceCounterReferencePort;
import net.pladema.medicalconsultation.appointment.domain.GetAppointmentReferenceClosureTypeBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetAppointment {

	private final FeatureFlagsService featureFlagsService;

	private final AppointmentPort appointmentPort;

	private final SharedReferenceCounterReference sharedReferenceCounterReference;

	private final AppointmentReferenceCounterReferencePort appointmentReferenceCounterReferencePort;

	public Optional<AppointmentBo> run(Integer appointmentId) {
		log.debug("Input parameter -> appointmentId {}", appointmentId);
		Optional<AppointmentBo> result = appointmentPort.getAppointmentById(appointmentId);
		result.ifPresent(this::completeAppointmentInformation);
		log.debug("Output -> {}", result);
		return result;
	}

	private void completeAppointmentInformation(AppointmentBo result) {
		setIsAppointmentProtected(result);
		setReferenceInfo(result);
		if (featureFlagsService.isOn(AppFeature.HABILITAR_RECURRENCIA_EN_DESARROLLO))
			handleRecurrentAppointment(result);
	}

	private void handleRecurrentAppointment(AppointmentBo result) {
		final int NO_CHILD_APPOINTMENTS = 0;
		Integer recurringAppointmentQuantity = appointmentPort.getRecurringAppointmentQuantityByAppointmentParentId(result.getId());
		result.setHasAppointmentChilds(!Objects.equals(recurringAppointmentQuantity, NO_CHILD_APPOINTMENTS));
	}

	private void setReferenceInfo(AppointmentBo result) {
		Optional<GetAppointmentReferenceClosureTypeBo> appointmentReferenceClosureType = appointmentReferenceCounterReferencePort.getReferenceClosureTypeByAppointmentId(result.getId());
		result.setHasAssociatedReference(appointmentReferenceClosureType.isPresent());
		result.setAssociatedReferenceClosureTypeId(appointmentReferenceClosureType.map(GetAppointmentReferenceClosureTypeBo::getAppointmentReferenceClosureTypeId).orElse(null));
	}

	private void setIsAppointmentProtected(AppointmentBo appointment) {
		List<Integer> protectedAppointments = sharedReferenceCounterReference.getProtectedAppointmentsIds(List.of(appointment.getDiaryId()));
		appointment.setProtected(protectedAppointments.contains(appointment.getId()));
	}

}
