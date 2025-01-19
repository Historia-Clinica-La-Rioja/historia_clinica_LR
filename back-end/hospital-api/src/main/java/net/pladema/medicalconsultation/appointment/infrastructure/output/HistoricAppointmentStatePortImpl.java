package net.pladema.medicalconsultation.appointment.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.application.port.HistoricAppointmentStatePort;
import net.pladema.medicalconsultation.appointment.domain.UpdateAppointmentStateBo;

import net.pladema.medicalconsultation.appointment.repository.HistoricAppointmentStateRepository;

import net.pladema.medicalconsultation.appointment.repository.entity.HistoricAppointmentState;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HistoricAppointmentStatePortImpl implements HistoricAppointmentStatePort {

	private final HistoricAppointmentStateRepository historicAppointmentStateRepository;

	@Override
	public void save(UpdateAppointmentStateBo updateAppointmentStateBo) {
		historicAppointmentStateRepository.save(new HistoricAppointmentState(updateAppointmentStateBo.getAppointmentId(), updateAppointmentStateBo.getAppointmentStateId(), updateAppointmentStateBo.getReason()));
	}

}
