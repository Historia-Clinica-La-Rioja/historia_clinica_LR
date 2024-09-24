package net.pladema.reports.domain.notification;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.notifications.templating.NotificationTemplateInput;
import net.pladema.reports.domain.GenerationReportNotificationArgs;

import java.util.Collections;

public class GenerationReportTemplateInput extends NotificationTemplateInput<GenerationReportNotificationArgs> {

    public final static String TEMPLATE_ID = "generation-report";

    public GenerationReportTemplateInput(GenerationReportNotificationArgs args, String subject) {
        super(TEMPLATE_ID, args, AppFeature.HABILITAR_NOTIFICACIONES_REPORTES_PROGRAMADOS, Collections.emptyList(), subject);
    }
}
