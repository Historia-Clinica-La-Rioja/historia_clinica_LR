package ar.lamansys.sgx.shared.notifications.infrastructure.controller;

import static ar.lamansys.sgx.shared.notifications.infrastructure.controller.NotificationTemplateInputUtils.NEW_APPOINTMENT_MESSAGE;
import static ar.lamansys.sgx.shared.notifications.infrastructure.controller.NotificationTemplateInputUtils.NEW_BOOKING_CONFIRM_MESSAGE;
import static ar.lamansys.sgx.shared.notifications.infrastructure.controller.NotificationTemplateInputUtils.NEW_GENERATION_REPORT_MESSAGE;
import static ar.lamansys.sgx.shared.notifications.infrastructure.controller.NotificationTemplateInputUtils.NEW_MEDICATION_REQUEST_MESSAGE;
import static ar.lamansys.sgx.shared.notifications.infrastructure.controller.NotificationTemplateInputUtils.NEW_RESTORE_PASSWORD_MESSAGE;
import static ar.lamansys.sgx.shared.notifications.infrastructure.controller.NotificationTemplateInputUtils.NEW_VIRTUAL_CONSULTATION_APPOINTMENT_MESSAGE;
import static ar.lamansys.sgx.shared.notifications.infrastructure.controller.NotificationTemplateInputUtils.RECIPIENT;

import java.util.ArrayList;
import java.util.List;

import net.pladema.reports.domain.notification.GenerationReportTemplateInput;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.online.infraestructure.notification.message.ConfirmarReservaTemplateInput;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.notifications.templating.NotificationTemplateInput;
import ar.lamansys.sgx.shared.notifications.templating.engine.impl.MailTemplateEngine;
import ar.lamansys.sgx.shared.templating.exceptions.TemplateException;
import lombok.AllArgsConstructor;
import net.pladema.clinichistory.requests.medicationrequests.service.impl.notification.NewMedicationRequestTemplateInput;
import net.pladema.medicalconsultation.appointment.infrastructure.output.notification.NewAppointmentTemplateInput;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.notification.VirtualConsultationAppointmentTemplateInput;
import net.pladema.user.infrastructure.output.notification.RestorePasswordTemplateInput;

@AllArgsConstructor
@RestController
@RequestMapping("/notification/mails")
public class NotificationTemplateController {
	private final FeatureFlagsService featureFlagsService;
	private final MailTemplateEngine mailTemplateEngine;

	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	@GetMapping
	public List<NotificationTemplateRenderDto> run() throws TemplateException {
		List<NotificationTemplateRenderDto> result = new ArrayList<>();
		result.add(newItem(NewAppointmentTemplateInput.TEMPLATE_ID, NEW_APPOINTMENT_MESSAGE));
		result.add(newItem(NewMedicationRequestTemplateInput.TEMPLATE_ID, NEW_MEDICATION_REQUEST_MESSAGE));
		result.add(newItem(RestorePasswordTemplateInput.TEMPLATE_ID, NEW_RESTORE_PASSWORD_MESSAGE));
		result.add(newItem(VirtualConsultationAppointmentTemplateInput.TEMPLATE_ID, NEW_VIRTUAL_CONSULTATION_APPOINTMENT_MESSAGE));
		result.add(newItem(ConfirmarReservaTemplateInput.TEMPLATE_ID, NEW_BOOKING_CONFIRM_MESSAGE));
		result.add(newItem(GenerationReportTemplateInput.TEMPLATE_ID, NEW_GENERATION_REPORT_MESSAGE));
		return result;
	}

	private NotificationTemplateRenderDto newItem(String templateId, NotificationTemplateInput<?> message) throws TemplateException {
		var mailMessageBo = mailTemplateEngine.process(RECIPIENT, message);
		boolean isEnabled = featureFlagsService.isOn(message.feature);
		return new NotificationTemplateRenderDto(templateId, mailMessageBo.subject, mailMessageBo.body, isEnabled);
	}

}
