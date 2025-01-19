package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Embeddable
@EqualsAndHashCode
public class DiagnosticReportTreePK implements Serializable {
	@Column(name = "diagnostic_report_parent_id", nullable = false)
	private Integer diagnosticReportParentId;

	@Column(name = "diagnostic_report_child_id", nullable = false)
	private Integer diagnosticReportChildId;
}
