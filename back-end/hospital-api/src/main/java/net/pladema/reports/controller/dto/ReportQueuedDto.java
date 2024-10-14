package net.pladema.reports.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class ReportQueuedDto {
	public final DateTimeDto createdOn;
	public final DateTimeDto generatedOn;
	public final String generatedError;
	public final boolean existsFile;
}
