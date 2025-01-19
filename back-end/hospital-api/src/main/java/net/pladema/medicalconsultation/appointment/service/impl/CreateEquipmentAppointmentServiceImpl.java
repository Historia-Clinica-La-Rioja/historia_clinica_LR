package net.pladema.medicalconsultation.appointment.service.impl;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.application.port.NewAppointmentNotification;
import net.pladema.medicalconsultation.appointment.domain.NewAppointmentNotificationBo;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.EquipmentAppointmentAssnRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import net.pladema.medicalconsultation.appointment.repository.entity.EquipmentAppointmentAssn;
import net.pladema.medicalconsultation.appointment.service.AppointmentOrderImageService;
import net.pladema.medicalconsultation.appointment.service.CreateEquipmentAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentOrderImageBo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class CreateEquipmentAppointmentServiceImpl implements CreateEquipmentAppointmentService {

	private final AppointmentRepository appointmentRepository;

	private final EquipmentAppointmentAssnRepository equipmentAppointmentAssnRepository;

	private final AppointmentOrderImageService appointmentOrderImageService;

	private final NewAppointmentNotification newAppointmentNotification;

	@Override
	@Transactional
	public AppointmentBo execute(AppointmentBo appointmentBo, Integer orderId, Integer studyId, Integer institutionId) {
		log.debug("Input parameters -> appointmentBo id {}", appointmentBo.getId());
		Appointment appointment = Appointment.newFromAppointmentBo(appointmentBo);
		appointment = appointmentRepository.save(appointment);
		AppointmentBo result = AppointmentBo.newFromAppointment(appointment);

		equipmentAppointmentAssnRepository.save(new EquipmentAppointmentAssn(
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

		AppointmentOrderImageBo appointmentOrderImageBO = new AppointmentOrderImageBo(appointment.getId(), orderId, studyId, false, null, institutionId);
		appointmentOrderImageService.save(appointmentOrderImageBO);

		log.debug("Output -> new AppointmentBo {}", (result != null) ? result.getId() : "null result");

		return result;
	}
}
