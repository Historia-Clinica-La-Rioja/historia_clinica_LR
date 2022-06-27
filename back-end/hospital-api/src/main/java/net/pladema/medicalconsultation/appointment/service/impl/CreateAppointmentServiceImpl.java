package net.pladema.medicalconsultation.appointment.service.impl;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.repository.AppointmentAssnRepository;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentAssn;
import net.pladema.medicalconsultation.appointment.service.CreateAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

@Slf4j
@AllArgsConstructor
@Service
public class CreateAppointmentServiceImpl implements CreateAppointmentService {

	private final AppointmentRepository appointmentRepository;

	private final AppointmentAssnRepository appointmentAssnRepository;


	@Override
	public AppointmentBo execute(AppointmentBo appointmentBo) {
		log.debug("Input parameters -> appointmentBo {}", appointmentBo);
		Appointment appointment = Appointment.newFromAppointmentBo(appointmentBo);
		appointment = appointmentRepository.save(appointment);
		AppointmentBo result = AppointmentBo.newFromAppointment(appointment);

		appointmentAssnRepository.save(new AppointmentAssn(
				appointmentBo.getDiaryId(),
				appointmentBo.getOpeningHoursId(),
				appointment.getId()
		));

		log.debug("Output -> {}", result);
		return result;
	}
}
