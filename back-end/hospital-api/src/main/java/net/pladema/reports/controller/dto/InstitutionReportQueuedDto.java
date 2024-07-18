package net.pladema.reports.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class InstitutionReportQueuedDto {
	public final Integer id;
	public final ReportQueuedDto report;
	public final DateDto startDate;
	public final DateDto endDate;
	public final Integer clinicalSpecialtyId;
	public final Integer doctorId;
	public final Integer hierarchicalUnitTypeId;
	public final Integer hierarchicalUnitId;
	public final boolean includeHierarchicalUnitDescendants;
}
