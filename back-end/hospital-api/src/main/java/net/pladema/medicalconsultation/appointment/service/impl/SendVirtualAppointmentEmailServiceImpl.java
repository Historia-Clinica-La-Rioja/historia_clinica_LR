package net.pladema.medicalconsultation.appointment.service.impl;

import ar.lamansys.sgh.shared.HospitalSharedAutoConfiguration;
import ar.lamansys.sgx.shared.emails.application.EmailNotificationChannel;
import ar.lamansys.sgx.shared.emails.domain.MailMessageBo;
import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;

import net.pladema.medicalconsultation.appointment.service.SendVirtualAppointmentEmailService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import net.pladema.person.repository.domain.CompletePersonNameBo;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.service.PersonService;

import net.pladema.staff.service.ClinicalSpecialtyService;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@Service
public class SendVirtualAppointmentEmailServiceImpl implements SendVirtualAppointmentEmailService {

	private static final String SUBJECT = "Turno de teleconsulta asignado exitosamente";

	public final TemplateEngine templateEngine;

	private final EmailNotificationChannel emailSender;

	private final ClinicalSpecialtyService clinicalSpecialtyService;

	private final PersonService personService;

	@Override
	public void run(AppointmentBo appointment) {
		log.debug("Input parameters -> appointment {}", appointment);
		Assert.notNull(HospitalSharedAutoConfiguration.JITSI_DOMAIN_URL, "No se encuentra configurada ninguna instancia de JITSI");
		String template = "digital-consultation-email";
		Person patient = personService.findByPatientId(appointment.getPatientId()).orElseThrow(() -> new IllegalStateException("No se encontró el paciente solicitado"));
		CompletePersonNameBo healthcareProfessional = personService.findByHealthcareProfessionalPersonDataByDiaryId(appointment.getDiaryId()).orElseThrow(() -> new IllegalStateException("No se encontró el profesional solicitado"));

		RecipientBo recipient;
		if (appointment.getModalityId().equals(EAppointmentModality.PATIENT_VIRTUAL_ATTENTION.getId()))
			recipient = new RecipientBo(patient.getFirstName(), patient.getLastName(), appointment.getPatientEmail(), appointment.getPhoneNumber());
		else
			recipient = new RecipientBo(healthcareProfessional.getPerson().getFirstName(), healthcareProfessional.getPerson().getLastName(), appointment.getApplicantHealthcareProfessionalEmail(), null);
		Map<String, Object> emailData = loadEmailData(appointment, patient, healthcareProfessional.getPersonFullName());
		sendTemplatedEmail(recipient, template, emailData);
	}

	private Map<String, Object> loadEmailData(AppointmentBo appointment, Person patient, String healthcareProfessionalName) {
		Map<String, Object> model = new HashMap<>();
		String clinicalSpecialtyDescription = clinicalSpecialtyService.getClinicalSpecialtyNameByDiaryId(appointment.getDiaryId());

		model.put("patientName", patient.getLastName() + " " + patient.getFirstName());
		model.put("date", appointment.getDate().toString() + " " + appointment.getHour().toString() + ".hs");
		model.put("professionalName", healthcareProfessionalName);
		model.put("clinicalSpecialty", clinicalSpecialtyDescription);
		model.put("callLink", HospitalSharedAutoConfiguration.JITSI_DOMAIN_URL + "/" + appointment.getCallId());
		return model;
	}

	private void sendTemplatedEmail(RecipientBo recipient, String template, Map<String,Object> variables) {
		try {
			Context context = new Context();
			context.setVariables(variables);
			String html = templateEngine.process(template, context);
			emailSender.send(recipient, new MailMessageBo(SUBJECT, html));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
