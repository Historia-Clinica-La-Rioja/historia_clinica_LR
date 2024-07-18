package net.pladema.reports.infrastructure.output.repository.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.reports.domain.InstitutionReportType;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "report_queue_institution")
public class InstitutionReportQueued {

	@Id
	@Column(name = "id")
	private Integer id;


	@Enumerated(EnumType.STRING)
	@Column(name = "report_type", length = 30)
	private InstitutionReportType reportType;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "date_start", nullable = false)
	private LocalDate startDate;

	@Column(name = "date_end", nullable = false)
	private LocalDate endDate;

	@Column(name = "clinical_specialty_id")
	private Integer clinicalSpecialtyId;

	@Column(name = "doctor_id")
	private Integer doctorId;

	@Column(name = "hierarchical_unit_type_id")
	private Integer hierarchicalUnitTypeId;

	@Column(name = "hierarchical_unit_id")
	private Integer hierarchicalUnitId;

	@Column(name = "include_hierarchical_unit_descendants")
	private Boolean includeHierarchicalUnitDescendants;

	@Column(name = "appointment_state_id")
	private Short appointmentStateId;

	public boolean isIncludeHierarchicalUnitDescendants() {
		return Boolean.TRUE.equals(includeHierarchicalUnitDescendants);
	}
}
