package net.pladema.reports.application.ports;

import java.time.LocalDateTime;

import net.pladema.reports.domain.InstitutionReportType;

public interface GenerationReportNotificationPort {

    void send(Integer userId, LocalDateTime createdOn, InstitutionReportType reportType);
}
