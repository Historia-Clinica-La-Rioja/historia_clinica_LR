package net.pladema.report.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UsageReportStatusDto {
	public final String domainId;
	public final boolean isAllowedToSend;
}
