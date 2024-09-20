package net.pladema.reports.infrastructure.output.notification;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.notifications.templating.NotificationTemplateInput;
import net.pladema.reports.domain.GenerationReportNotificationArgs;

public class GenerationReportTemplateInput extends NotificationTemplateInput<GenerationReportNotificationArgs> {

    public final static String TEMPLATE_ID = "generation-report";

    public GenerationReportTemplateInput(GenerationReportNotificationArgs args) {
        super(TEMPLATE_ID, args, AppFeature.HABILITAR_NOTIFICACIONES_REPORTES_PROGRAMADOS);
    }
}
