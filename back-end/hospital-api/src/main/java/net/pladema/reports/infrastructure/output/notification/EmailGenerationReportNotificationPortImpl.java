package net.pladema.reports.infrastructure.output.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.reports.application.ports.GenerationReportNotificationPort;
import net.pladema.reports.domain.GenerationReportNotificationArgs;
import net.pladema.reports.domain.InstitutionReportType;
import net.pladema.reports.domain.notification.GenerationReportTemplateInput;
import net.pladema.user.application.port.HospitalUserStorage;
import net.pladema.user.controller.service.domain.UserPersonInfoBo;
import net.pladema.user.infrastructure.output.notification.UserNotificationSender;
import net.pladema.user.infrastructure.output.notification.UserRecipient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailGenerationReportNotificationPortImpl implements GenerationReportNotificationPort {

    private final HospitalUserStorage hospitalUserStorage;
    private final UserNotificationSender userNotificationSender;

    public void send(Integer userId, LocalDateTime createdOn, InstitutionReportType reportType) {

        if (!hasEmail(userId)) {
            log.debug("User with id {} does not have an email address -> cannot send email", userId);
            return;
        }

        var notificationArgs = GenerationReportNotificationArgs.builder();
        notificationArgs.createdOn(createdOn);
        notificationArgs.reportType(reportType.getDescription());
        var subject = this.getSubject(reportType);

        userNotificationSender.send(
                new UserRecipient(userId),
                new GenerationReportTemplateInput(notificationArgs.build(), subject)
        );
    }

    private boolean hasEmail(Integer userId) {
        return hospitalUserStorage.getUserPersonInfo(userId)
                .map(UserPersonInfoBo::getEmail)
                .filter(email -> !email.isBlank())
                .isPresent();
    }

    private String getSubject(InstitutionReportType reportType) {
        return String.format("HSI - Reporte %s disponible para descarga", reportType.getDescription());
    }
}
