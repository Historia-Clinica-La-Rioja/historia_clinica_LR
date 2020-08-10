package net.pladema.medicalconsultation.appointment.service.impl;

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

	@Override
	public AppointmentBo execute(AppointmentBo appointmentBo) {
		LOG.debug("Input parameters -> appointmentBo {}", appointmentBo);
		appointmentAssnRepository.updateOpeningHoursId(appointmentBo.getOpeningHoursId(), appointmentBo.getId());
		LOG.debug("Output -> {}", appointmentBo);
		return appointmentBo;
	}
}
