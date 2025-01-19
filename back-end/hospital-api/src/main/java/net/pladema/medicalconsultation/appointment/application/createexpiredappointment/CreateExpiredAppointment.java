package net.pladema.medicalconsultation.appointment.application.createexpiredappointment;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.medicalconsultation.appointment.application.port.ExpiredAppointmentStorage;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentExpiredReason;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.CreateAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import net.pladema.medicalconsultation.appointment.service.exceptions.AppointmentEnumException;
import net.pladema.medicalconsultation.appointment.service.exceptions.AppointmentException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateExpiredAppointment {

	private final CreateAppointmentService createAppointmentService;

	private final InstitutionExternalService institutionExternalService;

	private final ExpiredAppointmentStorage expiredAppointmentStorage;

	private final static Integer NO_INSTITUTION = -1;

	@Transactional
	public Integer run(AppointmentBo appointmentBo, Integer institutionId) {
		log.debug("Input parameters -> appointmentBo {}", appointmentBo);
		validateExpiredAppointment(appointmentBo, institutionId);
		appointmentBo.setAppointmentStateId(AppointmentState.SERVED);
		AppointmentBo appointment = createAppointmentService.execute(appointmentBo);
		var appointmentId = appointment.getId();
		expiredAppointmentStorage.save(appointmentId, appointmentBo.getExpiredReasonId(), appointmentBo.getExpiredReasonText());
		return appointment.getId();
	}

	private void validateExpiredAppointment(AppointmentBo appointment, Integer institutionId) {
		if (!beforeNow(appointment, institutionId))
			throw new AppointmentException(AppointmentEnumException.EXPIRED_APPOINTMENT_INVALID_DATE_OR_HOUR, "Un turno vencido debe tener una fecha y hora anterior a la actual");
		if (appointment.getExpiredReasonId() == null)
			throw new AppointmentException(AppointmentEnumException.EXPIRED_APPOINTMENT_MISSING_REASON, "Es requerido ingresar el motivo por el cual se está registrando un turno vencido");
		if (appointment.getExpiredReasonId().equals(EAppointmentExpiredReason.OTHER.getId()) && appointment.getExpiredReasonText() == null)
			throw new AppointmentException(AppointmentEnumException.EXPIRED_APPOINTMENT_MISSING_REASON_TEXT, "Es requerido completar el campo de texto informando por qué se está registrando un turno vencido");
	}

	private boolean beforeNow(AppointmentBo appointmentBo, Integer institutionId) {
		ZoneId timezone = institutionId.equals(NO_INSTITUTION) ? ZoneId.of("UTC-3") : institutionExternalService.getTimezone(institutionId);

		ZonedDateTime apmtDateTime = LocalDateTime.of(appointmentBo.getDate(), appointmentBo.getHour())
				.atZone(timezone);

		ZonedDateTime nowInTimezone = LocalDateTime.now()
				.atZone(ZoneId.of(JacksonDateFormatConfig.UTC_ZONE_ID))
				.withZoneSameInstant(timezone);

		return apmtDateTime.isBefore(nowInTimezone);
	}
}
