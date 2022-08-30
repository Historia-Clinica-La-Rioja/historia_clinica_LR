package net.pladema.medicalconsultation.appointment.service.fetchappointments;


import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.AppointmentFilterBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.AppointmentInfoBo;
import net.pladema.medicalconsultation.appointment.service.ports.AppointmentStorage;

@Service
@Slf4j
public class FetchAppointments {

	private final AppointmentStorage appointmentStorage;

	public FetchAppointments(AppointmentStorage appointmentStorage) {
		this.appointmentStorage = appointmentStorage;
	}

	public List<AppointmentInfoBo> run(AppointmentFilterBo appointmentFilterBo) {
		log.debug("FetchAppointments  -> filter: {}", appointmentFilterBo);
		return appointmentStorage.fetchAppointments(appointmentFilterBo);
	}
}
