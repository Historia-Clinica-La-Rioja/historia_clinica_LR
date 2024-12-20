package ar.lamansys.sgx.shared.notifications.infrastructure.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import ar.lamansys.online.infraestructure.notification.message.ConfirmarReservaNotificationArgs;
import ar.lamansys.online.infraestructure.notification.message.ConfirmarReservaTemplateInput;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import net.pladema.clinichistory.requests.medicationrequests.service.impl.notification.NewMedicationRequestNotificationArgs;
import net.pladema.clinichistory.requests.medicationrequests.service.impl.notification.NewMedicationRequestTemplateInput;
import net.pladema.medicalconsultation.appointment.infrastructure.output.notification.NewAppointmentNotificationArgs;
import net.pladema.medicalconsultation.appointment.infrastructure.output.notification.NewAppointmentNotificationImpl;
import net.pladema.medicalconsultation.appointment.infrastructure.output.notification.NewAppointmentTemplateInput;
import net.pladema.medicalconsultation.appointment.service.impl.SendVirtualAppointmentEmailServiceImpl;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.notification.VirtualConsultationAppointmentTemplateArgs;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.notification.VirtualConsultationAppointmentTemplateInput;
import net.pladema.reports.domain.GenerationReportNotificationArgs;
import net.pladema.reports.domain.notification.GenerationReportTemplateInput;
import net.pladema.user.infrastructure.output.notification.RestorePasswordNotificationArgs;
import net.pladema.user.infrastructure.output.notification.RestorePasswordTemplateInput;

public class NotificationTemplateInputUtils {
	public static final RecipientBo RECIPIENT = new RecipientBo("Neil", "deGrasse Tyson", "neil@lamansys.com", "+5492490303456");

	public static final NewAppointmentTemplateInput NEW_APPOINTMENT_MESSAGE = buildNewAppointmentMessage();
	public static final NewMedicationRequestTemplateInput NEW_MEDICATION_REQUEST_MESSAGE = buildNewMedicationRequestMessage();
	public static final RestorePasswordTemplateInput NEW_RESTORE_PASSWORD_MESSAGE = buildRestorePasswordMessage();
	public static final VirtualConsultationAppointmentTemplateInput NEW_VIRTUAL_CONSULTATION_APPOINTMENT_MESSAGE = buildVirtualConsultationAppointmentMessage();
	public static final ConfirmarReservaTemplateInput NEW_BOOKING_CONFIRM_MESSAGE = buildBookingConfirmMessage();
	public static final GenerationReportTemplateInput NEW_GENERATION_REPORT_MESSAGE = buildGenerationReportMessage();
	private static final String PROFESSIONAL_FULLNAME = "Carl Sagan";
	private static final String CLINICAL_SPECIALTY = "Medicina General";

	private NotificationTemplateInputUtils() {}

	private static NewAppointmentTemplateInput buildNewAppointmentMessage() {
		return new NewAppointmentTemplateInput(NewAppointmentNotificationArgs.builder()
				.professionalFullName(PROFESSIONAL_FULLNAME)
				.specialty(CLINICAL_SPECIALTY)
				.day(NewAppointmentNotificationImpl.buildDayArg(LocalDate.now()))
				.time("10:38")
				.institution("Clínica del sol")
				.recomendation("...")
				.cancelationLink("")
				.fromFullName("HSI")
				.medicalCoverage("OS.OS")
				.doctorOffice("Consultorio 1")
				.identificationNumber("40303456")
				.build());
	}

	private static NewMedicationRequestTemplateInput buildNewMedicationRequestMessage() {
		return new NewMedicationRequestTemplateInput(NewMedicationRequestNotificationArgs.builder()
				.recipeId(1)
				.recipeIdWithDomain("AA-BBB")
				.patient(mockPatient())
				.build(), null);
	}

	private static RestorePasswordTemplateInput buildRestorePasswordMessage() {
		return new RestorePasswordTemplateInput(RestorePasswordNotificationArgs.builder()
				.fullname(RECIPIENT.firstname + " " + RECIPIENT.lastname)
				.link("/auth/password-recover/")
				.build());
	}

	private static VirtualConsultationAppointmentTemplateInput buildVirtualConsultationAppointmentMessage() {
		return new VirtualConsultationAppointmentTemplateInput(VirtualConsultationAppointmentTemplateArgs.builder()
				.patientName(RECIPIENT.lastname + " " + RECIPIENT.firstname)
				.date(SendVirtualAppointmentEmailServiceImpl.buildDateArg(LocalDate.now(), LocalTime.now()))
				.professionalName(PROFESSIONAL_FULLNAME)
				.clinicalSpecialty(CLINICAL_SPECIALTY)
				.callLink("https://meet.new/")
				.build());
	}

	private static ConfirmarReservaTemplateInput buildBookingConfirmMessage() {
		return new ConfirmarReservaTemplateInput(ConfirmarReservaNotificationArgs.builder()
				.cancelationLink("http://localhost:4700/link?code=uuid-uuid-uuh")
				.namePatient("Hugo Martinez")
				.date("13/12/2023 a las 18:00 h")
				.nameProfessional("Martina Hugarte")
				.specialty("Alergia")
				.institution("CAPS 11, Avellaneda 123")
				.recomendation("Llegar 15 minutos antes")
				.build());
	}

	private static GenerationReportTemplateInput buildGenerationReportMessage() {
		return new GenerationReportTemplateInput(GenerationReportNotificationArgs.builder()
				.reportType("Detalle nominal de turnos")
				.createdOn(LocalDateTime.of(2024, 10, 10, 15, 30))
				.build());
	}

	private static BasicPatientDto mockPatient() {
		return new BasicPatientDto(14, mockValidPerson(), (short)1);
	}

	private static BasicDataPersonDto mockValidPerson(){
		var result = new BasicDataPersonDto();
		result.setFirstName(RECIPIENT.firstname);
		result.setLastName(RECIPIENT.lastname);
		result.setIdentificationType("PASAPORTE");
		result.setIdentificationNumber("13694305");
		return result;
	}
}
