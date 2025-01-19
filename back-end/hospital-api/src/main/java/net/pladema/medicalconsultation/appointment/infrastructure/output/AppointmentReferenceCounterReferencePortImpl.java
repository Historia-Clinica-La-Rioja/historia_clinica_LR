package net.pladema.medicalconsultation.appointment.infrastructure.output;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceAppointmentStateDto;
import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.application.port.AppointmentReferenceCounterReferencePort;
import net.pladema.medicalconsultation.appointment.domain.GetAppointmentReferenceClosureTypeBo;

import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AppointmentReferenceCounterReferencePortImpl implements AppointmentReferenceCounterReferencePort {

	private final SharedReferenceCounterReference sharedReferenceCounterReference;

	@Override
	public Optional<GetAppointmentReferenceClosureTypeBo> getReferenceClosureTypeByAppointmentId(Integer appointmentId) {
		Optional<ReferenceAppointmentStateDto> optionalReferenceAppointmentStateDto = sharedReferenceCounterReference.getReferenceByAppointmentId(appointmentId);
		return optionalReferenceAppointmentStateDto.map(referenceAppointmentStateDto -> new GetAppointmentReferenceClosureTypeBo(referenceAppointmentStateDto.getReferenceId(), referenceAppointmentStateDto.getReferenceClosureTypeId()));
	}

}
