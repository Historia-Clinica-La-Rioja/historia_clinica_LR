package net.pladema.reports.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class ReportQueueItemBo {
	public final Integer id;
	public final LocalDateTime createdOn;
	public final LocalDateTime generatedOn;
	public final String generatedError;
	public final Long fileId;
}
