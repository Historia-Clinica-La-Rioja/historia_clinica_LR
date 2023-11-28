package net.pladema.medicalconsultation.appointment.application;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.appointment.domain.UpdateAppointmentDateBo;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;

import net.pladema.medicalconsultation.appointment.repository.EquipmentAppointmentAssnRepository;
import net.pladema.medicalconsultation.appointment.service.SendVirtualAppointmentEmailService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class ReassignAppointment {

	private AppointmentRepository appointmentRepository;

	private EquipmentAppointmentAssnRepository appointmentAssnRepository;

	private FeatureFlagsService featureFlagsService;

	private SendVirtualAppointmentEmailService sendVirtualAppointmentEmailService;

	public boolean run(UpdateAppointmentDateBo appointmentUpdateData) {
		log.debug("Input parameters -> appointmentUpdateData {}", appointmentUpdateData);
		appointmentRepository.updateDate(appointmentUpdateData.getAppointmentId(), appointmentUpdateData.getDate(), appointmentUpdateData.getTime());
		appointmentAssnRepository.updateOpeningHoursId(appointmentUpdateData.getOpeningHoursId(), appointmentUpdateData.getAppointmentId());
		if (mustSendEmail(appointmentUpdateData.getModality()))
			sendNotificationEmail(appointmentUpdateData);
		log.debug("Output -> {}", Boolean.TRUE);
		return Boolean.TRUE;
	}

	private boolean mustSendEmail(EAppointmentModality modality) {
		return featureFlagsService.isOn(AppFeature.HABILITAR_TELEMEDICINA) &&
				(modality.equals(EAppointmentModality.PATIENT_VIRTUAL_ATTENTION) ||
						modality.equals(EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION));
	}

	private void sendNotificationEmail(UpdateAppointmentDateBo appointmentUpdateData) {
		AppointmentBo appointmentBo = appointmentRepository.getEmailNotificationData(appointmentUpdateData.getAppointmentId());
		appointmentBo.setModalityId(appointmentUpdateData.getModality().getId());
		if (appointmentUpdateData.getPatientEmail() != null) {
			appointmentBo.setPatientEmail(appointmentUpdateData.getPatientEmail());
			appointmentRepository.updateAppointmentPatientEmail(appointmentUpdateData.getAppointmentId(), appointmentUpdateData.getPatientEmail());
		}
		sendVirtualAppointmentEmailService.run(appointmentBo);
	}

}
