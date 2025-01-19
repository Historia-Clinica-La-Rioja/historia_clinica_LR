package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fhir_diagnostic_report_performer_practitioner")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FhirDiagnosticReportPerformerPractitioner {
	@Id
	@Column(name = "diagnostic_report_id", nullable = false)
	private Integer diagnosticReportId;
	@Column(name = "identification_number", nullable = false)
	private String identificationNumber;
	@Column(name = "first_name", nullable = false)
	private String firstName;
	@Column(name = "last_name", nullable = false)
	private String lastName;
}
