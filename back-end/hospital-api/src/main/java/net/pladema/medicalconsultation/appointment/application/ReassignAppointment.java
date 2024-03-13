package net.pladema.medicalconsultation.appointment.application;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.appointment.domain.UpdateAppointmentDateBo;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;

import net.pladema.medicalconsultation.appointment.repository.EquipmentAppointmentAssnRepository;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.SendVirtualAppointmentEmailService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@AllArgsConstructor
@Slf4j
@Service
public class ReassignAppointment {

	private AppointmentRepository appointmentRepository;

	private EquipmentAppointmentAssnRepository appointmentAssnRepository;

	private FeatureFlagsService featureFlagsService;

	private SendVirtualAppointmentEmailService sendVirtualAppointmentEmailService;

	private AppointmentService appointmentService;

	@Transactional
	public boolean run(UpdateAppointmentDateBo appointmentUpdateData) {
		log.debug("Input parameters -> appointmentUpdateData {}", appointmentUpdateData);
		appointmentRepository.updateDate(appointmentUpdateData.getAppointmentId(), appointmentUpdateData.getDate(), appointmentUpdateData.getTime());
		appointmentAssnRepository.updateOpeningHoursId(appointmentUpdateData.getOpeningHoursId(), appointmentUpdateData.getAppointmentId());
		updateRecurringType(appointmentUpdateData);
		AppointmentBo appointment = appointmentRepository.getEmailNotificationData(appointmentUpdateData.getAppointmentId());
		updateModalityRelatedData(appointment, appointmentUpdateData);
		if (mustSendEmail(appointmentUpdateData.getModality()))
			sendNotificationEmail(appointment, appointmentUpdateData);
		log.debug("Output -> {}", Boolean.TRUE);
		return Boolean.TRUE;
	}

	private void updateModalityRelatedData(AppointmentBo appointment, UpdateAppointmentDateBo appointmentUpdateData) {
		boolean modalityChanged = !appointment.getModalityId().equals(appointmentUpdateData.getModality().getId());
		if (modalityChanged && appointmentUpdateData.getModality().equals(EAppointmentModality.ON_SITE_ATTENTION))
			appointmentRepository.removeVirtualAttentionAttributes(appointmentUpdateData.getAppointmentId());
		appointmentRepository.updateAppointmentModalityId(appointmentUpdateData.getAppointmentId(), appointmentUpdateData.getModality().getId());
	}

	private boolean mustSendEmail(EAppointmentModality modality) {
		return featureFlagsService.isOn(AppFeature.HABILITAR_TELEMEDICINA) &&
				(modality.equals(EAppointmentModality.PATIENT_VIRTUAL_ATTENTION) ||
						modality.equals(EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION));
	}

	private void sendNotificationEmail(AppointmentBo appointment, UpdateAppointmentDateBo appointmentUpdateData) {
		appointment.setModalityId(appointmentUpdateData.getModality().getId());
		if (appointmentUpdateData.getPatientEmail() != null)
			updatePatientEmail(appointmentUpdateData, appointment);
		if (appointment.getCallId() == null)
			updateCallId(appointment, appointmentUpdateData);
		sendVirtualAppointmentEmailService.run(appointment);
	}

	private void updateCallId(AppointmentBo appointment, UpdateAppointmentDateBo appointmentUpdateData) {
		String callId = UUID.randomUUID().toString();
		appointmentRepository.updateCallId(appointmentUpdateData.getAppointmentId(), callId);
		appointment.setCallId(callId);
	}

	private void updatePatientEmail(UpdateAppointmentDateBo appointmentUpdateData, AppointmentBo appointmentBo) {
		appointmentBo.setPatientEmail(appointmentUpdateData.getPatientEmail());
		appointmentRepository.updateAppointmentPatientEmail(appointmentUpdateData.getAppointmentId(), appointmentUpdateData.getPatientEmail());
	}

	private void updateRecurringType(UpdateAppointmentDateBo appointmentUpdateData) {
		if (featureFlagsService.isOn(AppFeature.HABILITAR_RECURRENCIA_EN_DESARROLLO)) {
			appointmentRepository.updateRecurringType(appointmentUpdateData.getAppointmentId(), appointmentUpdateData.getRecurringAppointmentTypeId());
			appointmentService.getAppointment(appointmentUpdateData.getAppointmentId())
					.ifPresent(app -> appointmentService.verifyRecurringAppointmentsOverturn(app.getDiaryId()));
		}
	}

}
