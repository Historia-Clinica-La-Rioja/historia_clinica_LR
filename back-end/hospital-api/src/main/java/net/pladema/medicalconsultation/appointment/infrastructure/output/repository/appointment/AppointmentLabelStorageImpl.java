package net.pladema.medicalconsultation.appointment.infrastructure.output.repository.appointment;

import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.appointment.repository.AppointmentLabelRepository;
import net.pladema.medicalconsultation.appointment.service.ports.AppointmentLabelStorage;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class AppointmentLabelStorageImpl implements AppointmentLabelStorage {

	private static final String OUTPUT = "Output -> {}";

	private final AppointmentLabelRepository appointmentLabelRepository;

	@Override
	public boolean updateLabel(Integer diaryLabelId, Integer appointmentId) {
		log.debug("Input parameters -> diaryLabelId {}, appointmentId {}", diaryLabelId, appointmentId);
		Integer userId = UserInfo.getCurrentAuditor();
		appointmentLabelRepository.updateLabel(diaryLabelId, userId, appointmentId);
		log.debug(OUTPUT, true);
		return true;
	}
}
