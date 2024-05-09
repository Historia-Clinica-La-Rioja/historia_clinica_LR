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
@ToString
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosticReportFhirObservationGroupPK implements Serializable {

	@Column(name = "fhir_observation_group_id", nullable = false)
	private Integer fhirObservationGroupId;

	@Column(name = "diagnostic_report_id", nullable = false)
	private Integer diagnosticReportId;

}
