package net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.notification;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.notifications.templating.NotificationTemplateInput;

public class VirtualConsultationAppointmentTemplateInput extends NotificationTemplateInput<VirtualConsultationAppointmentTemplateArgs> {

	public final static String TEMPLATE_ID = "digital-consultation-email";

	public VirtualConsultationAppointmentTemplateInput(VirtualConsultationAppointmentTemplateArgs args) {
		super(TEMPLATE_ID, args, AppFeature.HABILITAR_TELEMEDICINA);
	}

}
