package net.pladema.medicalconsultation.appointment.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;

import ar.lamansys.sgh.shared.HospitalSharedAutoConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;

import net.pladema.medicalconsultation.appointment.service.SendVirtualAppointmentEmailService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.notification.VirtualConsultationAppointmentTemplateArgs;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.notification.VirtualConsultationAppointmentTemplateInput;
import net.pladema.patient.infrastructure.output.notification.PatientNotificationSender;
import net.pladema.patient.infrastructure.output.notification.PatientRecipient;
import net.pladema.person.infraestructure.output.notification.PersonNotificationSender;
import net.pladema.person.infraestructure.output.notification.PersonRecipient;
import net.pladema.person.repository.domain.CompletePersonNameBo;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.service.PersonService;

import net.pladema.staff.service.ClinicalSpecialtyService;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@AllArgsConstructor
@Slf4j
@Service
public class SendVirtualAppointmentEmailServiceImpl implements SendVirtualAppointmentEmailService {

	private final PatientNotificationSender patientNotificationSender;

	private final ClinicalSpecialtyService clinicalSpecialtyService;

	private final PersonService personService;

	private final PersonNotificationSender personNotificationSender;

	@Override
	public void run(AppointmentBo appointment) {
		log.debug("Input parameters -> appointment {}", appointment);
		Assert.isTrue(HospitalSharedAutoConfiguration.JITSI_DOMAIN_URL != null && !HospitalSharedAutoConfiguration.JITSI_DOMAIN_URL.isEmpty(), "No se encuentra configurada ninguna instancia de JITSI");
		CompletePersonNameBo healthcareProfessional = personService.findByHealthcareProfessionalPersonDataByDiaryId(appointment.getDiaryId()).orElseThrow(() -> new IllegalStateException("No se encontró el profesional solicitado"));
		VirtualConsultationAppointmentTemplateArgs args = generateArgs(appointment, healthcareProfessional.getPersonFullName());
		if (appointment.getModalityId().equals(EAppointmentModality.PATIENT_VIRTUAL_ATTENTION.getId()))
			patientNotificationSender.send(new PatientRecipient(appointment.getPatientId(), appointment.getPatientEmail()), new VirtualConsultationAppointmentTemplateInput(args));
		else
			personNotificationSender.send(new PersonRecipient(healthcareProfessional.getPerson().getId(), appointment.getApplicantHealthcareProfessionalEmail()), new VirtualConsultationAppointmentTemplateInput(args));
	}

	private VirtualConsultationAppointmentTemplateArgs generateArgs(AppointmentBo appointment, String healthcareProfessionalName) {
		Person patient = personService.findByPatientId(appointment.getPatientId()).orElseThrow(() -> new IllegalStateException("No se encontró el paciente solicitado"));
		String clinicalSpecialtyDescription = clinicalSpecialtyService.getClinicalSpecialtyNameByDiaryId(appointment.getDiaryId());
		return VirtualConsultationAppointmentTemplateArgs.builder()
				.patientName(patient.getLastName() + " " + patient.getFirstName())
				.date(buildDateArg(appointment.getDate(), appointment.getHour()))
				.professionalName(healthcareProfessionalName)
				.clinicalSpecialty(clinicalSpecialtyDescription)
				.callLink(HospitalSharedAutoConfiguration.JITSI_DOMAIN_URL + "/" + appointment.getCallId())
				.build();
	}

	public static String buildDateArg(LocalDate date, LocalTime hour) {
		return date.toString() + " " + hour.toString() + ".hs";
	}

}
