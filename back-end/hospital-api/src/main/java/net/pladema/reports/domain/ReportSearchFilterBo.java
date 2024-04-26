package net.pladema.reports.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReportSearchFilterBo {

	private LocalDate fromDate;

	private LocalDate toDate;

	private Integer institutionId;

	private Integer clinicalSpecialtyId;

	private Integer doctorId;

	private Integer hierarchicalUnitTypeId;

	private Integer hierarchicalUnitId;

	private Short appointmentStateId;

	private boolean includeHierarchicalUnitDescendants;

}
