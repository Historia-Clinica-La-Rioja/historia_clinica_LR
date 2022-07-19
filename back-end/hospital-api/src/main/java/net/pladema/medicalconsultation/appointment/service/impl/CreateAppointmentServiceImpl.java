package net.pladema.medicalconsultation.appointment.service.impl;

import ar.lamansys.sgx.shared.featureflags.AppFeature;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.application.port.NewAppointmentNotification;
import net.pladema.medicalconsultation.appointment.domain.NewAppointmentNotificationBo;
import net.pladema.medicalconsultation.appointment.repository.AppointmentAssnRepository;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentAssn;
import net.pladema.medicalconsultation.appointment.service.CreateAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class CreateAppointmentServiceImpl implements CreateAppointmentService {

	private final AppointmentRepository appointmentRepository;

	private final AppointmentAssnRepository appointmentAssnRepository;

	private final NewAppointmentNotification newAppointmentNotification;

	@Override
	@Transactional
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

		if(appointment.getPatientId()!=null && AppFeature.HABILITAR_NOTIFICACIONES_TURNOS.isActive())
			newAppointmentNotification.run(new NewAppointmentNotificationBo(
				appointment.getPatientId(),
				appointment.getPatientMedicalCoverageId(),
				appointment.getDateTypeId(),
				appointment.getHour(),
				appointmentBo.getDiaryId()
			));

		log.debug("Output -> {}", result);
		return result;
	}
}
