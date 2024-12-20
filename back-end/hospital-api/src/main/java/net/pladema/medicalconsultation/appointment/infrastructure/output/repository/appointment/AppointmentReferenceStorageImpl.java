package net.pladema.medicalconsultation.appointment.infrastructure.output.repository.appointment;

import ar.lamansys.sgh.shared.domain.reference.ReferencePhoneBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferencePhoneDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.application.port.AppointmentReferenceStorage;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppointmentReferenceStorageImpl implements AppointmentReferenceStorage {

	private final SharedReferenceCounterReference sharedReferenceCounterReference;

	@Override
	public ReferencePhoneBo getReferencePhoneData(Integer referenceId) {
		log.debug("Input parameter -> {} ", referenceId);
		ReferencePhoneDto referencePhone = sharedReferenceCounterReference.getReferencePhoneData(referenceId);
		return mapToReferencePhoneBo(referencePhone);
	}

	@Override
	public void associateReferenceToAppointment(Integer referenceId, Integer appointmentId, Boolean isProtected, Integer institutionId) {
		log.debug("Input parameter -> referenceId {}, appointmentId {}, isProtected {}, institutionId {} ", referenceId, appointmentId, isProtected, institutionId);
		sharedReferenceCounterReference.associateReferenceToAppointment(referenceId, appointmentId, isProtected, institutionId);
	}

	private ReferencePhoneBo mapToReferencePhoneBo(ReferencePhoneDto referencePhoneDto) {
		return new ReferencePhoneBo(referencePhoneDto.getPhonePrefix(), referencePhoneDto.getPhoneNumber());
	}
}
