package net.pladema.clinichistory.requests.medicationrequests.service.impl.notification;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.templating.NotificationTemplateInput;

/**
 * Define el nombre del template y los argumentos que se requieren como input para renderizar el mensaje.
 */
public class NewMedicationRequestTemplateInput extends NotificationTemplateInput<NewMedicationRequestNotificationArgs> {
	public final static String TEMPLATE_ID = "digital-recipe";

	public NewMedicationRequestTemplateInput(NewMedicationRequestNotificationArgs args, String subject) {
		super(TEMPLATE_ID, args, AppFeature.HABILITAR_RECETA_DIGITAL, args.resources, subject);
	}
}