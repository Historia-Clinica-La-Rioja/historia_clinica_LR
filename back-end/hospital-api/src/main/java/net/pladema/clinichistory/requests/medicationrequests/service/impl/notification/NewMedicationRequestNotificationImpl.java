package net.pladema.clinichistory.requests.medicationrequests.service.impl.notification;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.medicationrequests.service.NewMedicationRequestNotification;
import net.pladema.patient.infrastructure.output.notification.PatientNotificationSender;
import net.pladema.patient.infrastructure.output.notification.PatientRecipient;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewMedicationRequestNotificationImpl implements NewMedicationRequestNotification {

	private final PatientNotificationSender patientNotificationSender;


	@Override
	public void run(NewMedicationRequestNotificationArgs args) {
		log.debug("Input parameters -> args {}", args);
		this.patientNotificationSender.send(new PatientRecipient(args.getPatient().getId()), new NewMedicationRequestTemplateInput(args));
	}
}
