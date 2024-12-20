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
@Table(name = "fhir_diagnostic_report_performer_organization")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FhirDiagnosticReportPerformerOrganization {
	@Id
	@Column(name = "diagnostic_report_id", nullable = false)
	private Integer diagnosticReportId;
	@Column(name = "name", nullable = true)
	private String name;
	@Column(name = "address", nullable = true)
	private String address;
	@Column(name = "city", nullable = true)
	private String city;
	@Column(name = "postcode", nullable = true)
	private String postcode;
	@Column(name = "province", nullable = true)
	private String province;
	@Column(name = "country", nullable = true)
	private String country;
	@Column(name = "phone_number", nullable = true)
	private String phoneNumber;
	@Column(name = "email", nullable = true)
	private String email;

}
