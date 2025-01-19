package net.pladema.reports.application;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@AllArgsConstructor
@Builder
@ToString
public class ReportInstitutionQueryBo {
	public final Integer institutionId;
	public final LocalDate startDate;
	public final LocalDate endDate;
	public final Integer clinicalSpecialtyId;
	public final Integer doctorId;
	public final Integer hierarchicalUnitTypeId;
	public final Integer hierarchicalUnitId;
	public final boolean includeHierarchicalUnitDescendants;
	public final Short appointmentStateId;
}
