package net.pladema.medicalconsultation.appointment.infraestructure.output.notification;
import net.pladema.establishment.service.InstitutionService;
import net.pladema.establishment.service.domain.InstitutionBo;
import net.pladema.medicalconsultation.appointment.infraestructure.output.notification.exceptions.NewAppointmentNotificationEnumException;
import net.pladema.medicalconsultation.appointment.infraestructure.output.notification.exceptions.NewAppointmentNotificationException;
import net.pladema.medicalconsultation.appointment.service.booking.BookingPersonService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.CompleteDiaryBo;
import net.pladema.patient.service.PatientMedicalCoverageService;
import net.pladema.patient.service.PatientService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.application.port.NewAppointmentNotification;
import net.pladema.medicalconsultation.appointment.domain.NewAppointmentNotificationBo;
import net.pladema.patient.infraestructure.output.notification.PatientNotificationSender;
import net.pladema.patient.infraestructure.output.notification.PatientRecipient;

import java.sql.Date;
import java.text.SimpleDateFormat;

@AllArgsConstructor
@Slf4j
@Service
public class NewAppointmentNotificationImpl implements NewAppointmentNotification {
	private final PatientNotificationSender patientNotificationSender;
	private final BookingPersonService bookingPersonService;
	private final InstitutionService institutionService;
	private final DiaryService diaryService;
	private final PatientMedicalCoverageService patientMedicalCoverageService;
	private final PatientService patientService;
	private final Environment env;

	@Override
	public void run(NewAppointmentNotificationBo newAppointmentNotification) {
		CompleteDiaryBo diaryBo = diaryService.getDiary(newAppointmentNotification.diaryId)
				.orElseThrow(()-> new NewAppointmentNotificationException(NewAppointmentNotificationEnumException.DIARY_NOT_FOUND, "La agenda solicitada no existe"));
		String professionalName = bookingPersonService.getProfessionalName(newAppointmentNotification.diaryId)
				.orElseThrow(()-> new NewAppointmentNotificationException(NewAppointmentNotificationEnumException.PROFESSIONAL_NAME_NOT_FOUND, String.format("No se encontro el profesional de la agenda con id ",newAppointmentNotification.diaryId)));
		InstitutionBo institutionBo = institutionService.get(diaryService.getInstitution(newAppointmentNotification.diaryId));
		String day = StringUtils.capitalize(new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY").format(Date.valueOf(newAppointmentNotification.dateTypeId)));
		String medicalCoverage = (newAppointmentNotification.patientMedicalCoverageId !=null) ? patientMedicalCoverageService.getCoverage(newAppointmentNotification.patientMedicalCoverageId)
				.map(r-> r.getMedicalCoverage().getName()).orElse(null) : null;
		String identificationNumber = patientService.getIdentificationNumber(newAppointmentNotification.patientId).orElse(null);

		var notificationArgs = NewAppointmentNotificationArgs.builder();
		// se resuelven los argumentos que requiere el mensaje a enviar a partir del BO
		notificationArgs
				.professionalFullName(professionalName)
				.day(day)
				.time(String.format("%s", newAppointmentNotification.hour))
				.institution(institutionBo.getName())
				.recomendation("...")
				.specialty(diaryBo.getClinicalSpecialtyName())
				.fromFullName(env.getProperty("app.notification.mail.fromFullname"))
				.doctorOffice(diaryBo.getDoctorsOfficeDescription())
				.medicalCoverage(medicalCoverage)
				.identificationNumber(identificationNumber);
		this.patientNotificationSender.send(
				new PatientRecipient(newAppointmentNotification.patientId),
				new NewAppointmentTemplateInput(
						notificationArgs.build()
				)
		);
	}

}
