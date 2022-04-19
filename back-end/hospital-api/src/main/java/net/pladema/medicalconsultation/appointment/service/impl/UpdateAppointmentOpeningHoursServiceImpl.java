package net.pladema.medicalconsultation.appointment.service.impl;

import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;

import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.repository.AppointmentAssnRepository;
import net.pladema.medicalconsultation.appointment.service.UpdateAppointmentOpeningHoursService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

@Service
@RequiredArgsConstructor
public class UpdateAppointmentOpeningHoursServiceImpl implements UpdateAppointmentOpeningHoursService {

	private static final Logger LOG = LoggerFactory.getLogger(UpdateAppointmentOpeningHoursServiceImpl.class);

	private final AppointmentAssnRepository appointmentAssnRepository;

	private final AppointmentRepository appointmentRepository;

	@Override
	public AppointmentBo execute(AppointmentBo appointmentBo) {
		LOG.debug("Input parameters -> appointmentBo {}", appointmentBo);
		appointmentAssnRepository.updateOpeningHoursId(appointmentBo.getOpeningHoursId(), appointmentBo.getId());
		if(appointmentBo.getAppointmentStateId().equals(AppointmentState.OUT_OF_DIARY)) {
			if(appointmentBo.getPatientId() != null)
				appointmentRepository.updateState(appointmentBo.getId(), AppointmentState.ASSIGNED, UserInfo.getCurrentAuditor());
			else
				appointmentRepository.updateState(appointmentBo.getId(), AppointmentState.BOOKED, UserInfo.getCurrentAuditor());
		}

		LOG.debug("Output -> {}", appointmentBo);
		return appointmentBo;
	}
}
