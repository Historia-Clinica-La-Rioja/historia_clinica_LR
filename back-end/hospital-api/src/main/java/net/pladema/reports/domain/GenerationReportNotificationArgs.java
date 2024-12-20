package net.pladema.reports.domain;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class GenerationReportNotificationArgs {

    public final String reportType;
    public final LocalDateTime createdOn;

}
