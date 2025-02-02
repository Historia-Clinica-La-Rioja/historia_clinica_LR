package net.pladema.medicalconsultation.appointment.infrastructure.output.notification;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.service.InstitutionService;
import net.pladema.establishment.service.domain.InstitutionBo;
import net.pladema.medicalconsultation.appointment.application.port.NewAppointmentNotification;
import net.pladema.medicalconsultation.appointment.domain.NewAppointmentNotificationBo;
import net.pladema.medicalconsultation.appointment.infrastructure.output.notification.exceptions.NewAppointmentNotificationEnumException;
import net.pladema.medicalconsultation.appointment.infrastructure.output.notification.exceptions.NewAppointmentNotificationException;
import net.pladema.medicalconsultation.appointment.service.booking.BookingPersonService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.doctorsoffice.service.DoctorsOfficeService;
import net.pladema.patient.infrastructure.output.notification.PatientNotificationSender;
import net.pladema.patient.infrastructure.output.notification.PatientRecipient;
import net.pladema.patient.service.PatientMedicalCoverageService;
import net.pladema.patient.service.PatientService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

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
	private final DoctorsOfficeService doctorsOfficeService;

	@Override
	public void run(NewAppointmentNotificationBo newAppointmentNotification) {
		DiaryBo diaryBo = diaryService.getDiaryById(newAppointmentNotification.diaryId);
		String professionalName = bookingPersonService.getProfessionalName(newAppointmentNotification.diaryId)
				.orElseThrow(()-> new NewAppointmentNotificationException(NewAppointmentNotificationEnumException.PROFESSIONAL_NAME_NOT_FOUND, String.format("No se encontro el profesional de la agenda con id ",newAppointmentNotification.diaryId)));
		InstitutionBo institutionBo = institutionService.get(diaryService.getInstitution(newAppointmentNotification.diaryId));

		String medicalCoverage = (newAppointmentNotification.patientMedicalCoverageId !=null) ? patientMedicalCoverageService.getCoverage(newAppointmentNotification.patientMedicalCoverageId)
				.map(r-> r.getMedicalCoverage().getName()).orElse(null) : null;
		String identificationNumber = patientService.getIdentificationNumber(newAppointmentNotification.patientId).orElse(null);
		this.setDoctorsOfficeDescription(diaryBo);

		var notificationArgs = NewAppointmentNotificationArgs.builder();
		// se resuelven los argumentos que requiere el mensaje a enviar a partir del BO
		notificationArgs
				.professionalFullName(professionalName)
				.day(buildDayArg(newAppointmentNotification.dateTypeId))
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

	private void setDoctorsOfficeDescription(DiaryBo diaryBo) {
		Optional.ofNullable(diaryBo.getDoctorsOfficeId())
				.ifPresent((doctorOfficeId) -> {
					String officeName = doctorsOfficeService.getDescription(doctorOfficeId);
					diaryBo.setDoctorsOfficeDescription(officeName);
				});
	}

	public static String buildDayArg(LocalDate date) {
		return StringUtils.capitalize(new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY").format(Date.valueOf(date)));
	}
}
