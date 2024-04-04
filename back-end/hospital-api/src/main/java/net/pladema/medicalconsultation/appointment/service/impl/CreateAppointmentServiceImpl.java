package net.pladema.medicalconsultation.appointment.service.impl;

import ar.lamansys.sgh.shared.domain.reference.ReferencePhoneBo;
import ar.lamansys.sgx.shared.featureflags.AppFeature;

import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.medicalconsultation.appointment.application.port.AppointmentReferenceStorage;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.SendVirtualAppointmentEmailService;
import net.pladema.medicalconsultation.appointment.service.exceptions.AppointmentEnumException;
import net.pladema.medicalconsultation.appointment.service.exceptions.AppointmentException;

import net.pladema.medicalconsultation.diary.repository.DiaryOpeningHoursRepository;

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

import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@AllArgsConstructor
@Service
public class CreateAppointmentServiceImpl implements CreateAppointmentService {

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	private final AppointmentRepository appointmentRepository;

	private final AppointmentAssnRepository appointmentAssnRepository;

	private final NewAppointmentNotification newAppointmentNotification;

	private final DiaryOpeningHoursRepository diaryOpeningHoursRepository;

	private final SendVirtualAppointmentEmailService sendVirtualAppointmentEmailService;

	private final AppointmentService appointmentService;

	private final AppointmentReferenceStorage appointmentReferenceStorage;

	@Override
	@Transactional
	public AppointmentBo execute(AppointmentBo appointmentBo) {
		log.debug("Input parameters -> appointmentBo {}", appointmentBo);
		validateAppointment(appointmentBo);
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

		if (appointmentBo.getModalityId().equals(EAppointmentModality.PATIENT_VIRTUAL_ATTENTION.getId()) || appointmentBo.getModalityId().equals(EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION.getId()))
			sendVirtualAppointmentEmailService.run(appointmentBo);

		if (appointmentBo.getReferenceId() != null) {
			boolean appointmentHasPhone = appointmentBo.getPhoneNumber() != null &&  appointmentBo.getPhonePrefix() != null;
			associateReference(appointment.getId(), appointmentBo.getReferenceId(), appointmentBo.getOpeningHoursId(), appointmentBo.getDiaryId(), appointmentHasPhone);
		}

		log.debug("Output -> {}", result);
		return result;
	}

	private void associateReference(Integer appointmentId, Integer referenceId, Integer openingHoursId, Integer diaryId, boolean appointmentHasPhone) {
		boolean isProtected = appointmentService.openingHourAllowedProtectedAppointment(openingHoursId, diaryId);
		appointmentReferenceStorage.associateReferenceToAppointment(referenceId, appointmentId, isProtected);
		if (!appointmentHasPhone) {
			ReferencePhoneBo phoneReference = appointmentReferenceStorage.getReferencePhoneData(referenceId);
			appointmentService.updatePhoneNumber(appointmentId, phoneReference.getPhonePrefix(), phoneReference.getPhoneNumber(), UserInfo.getCurrentAuditor());
		}
	}

	private void validateAppointment(AppointmentBo appointment) {
		Boolean isPatientVirtualConsultationAllowed = diaryOpeningHoursRepository.isPatientVirtualConsultationAllowed(appointment.getDiaryId(), appointment.getOpeningHoursId());
		Boolean isSecondOpinionVirtualConsultationAllowed = diaryOpeningHoursRepository.isSecondOpinionVirtualConsultationAllowed(appointment.getDiaryId(), appointment.getOpeningHoursId());
		if (isPatientVirtualConsultationAllowed != null && isPatientVirtualConsultationAllowed && appointment.getModalityId().equals(EAppointmentModality.PATIENT_VIRTUAL_ATTENTION.getId())) {
			if (appointment.getPatientEmail() == null)
				throw new AppointmentException(AppointmentEnumException.MISSING_DATA, "Se requiere el correo del paciente para la modalidad seleccionada");
			if (!VALID_EMAIL_ADDRESS_REGEX.matcher(appointment.getPatientEmail()).matches())
				throw new AppointmentException(AppointmentEnumException.WRONG_EMAIL_FORMAT, "El formato del correo electrónico del paciente ingresado no es válido");
			appointment.setCallId(UUID.randomUUID().toString());
		}
		if (isSecondOpinionVirtualConsultationAllowed != null && isSecondOpinionVirtualConsultationAllowed && appointment.getModalityId().equals(EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION.getId())) {
			if (appointment.getApplicantHealthcareProfessionalEmail() == null)
				throw new AppointmentException(AppointmentEnumException.MISSING_DATA, "Se requiere el correo del profesional solicitante para la modalidad seleccionada");
			if (!VALID_EMAIL_ADDRESS_REGEX.matcher(appointment.getApplicantHealthcareProfessionalEmail()).matches())
				throw new AppointmentException(AppointmentEnumException.WRONG_EMAIL_FORMAT, "El formato del correo electrónico del profesional ingresado no es válido");
			appointment.setCallId(UUID.randomUUID().toString());
		}
	}
}
