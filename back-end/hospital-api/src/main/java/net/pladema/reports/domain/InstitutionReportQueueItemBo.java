package net.pladema.reports.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class InstitutionReportQueueItemBo {
	public final ReportQueueItemBo queued;
	// filtro
	public final LocalDate startDate;
	public final LocalDate endDate;
	public final Integer clinicalSpecialtyId;
	public final Integer doctorId;
	public final Integer hierarchicalUnitTypeId;
	public final Integer hierarchicalUnitId;
	public final boolean includeHierarchicalUnitDescendants;
	public final Short appointmentStateId;
}
