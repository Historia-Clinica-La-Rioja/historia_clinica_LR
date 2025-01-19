package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "diagnostic_report_fhir_observation_group")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DiagnosticReportFhirObservationGroup implements Serializable {

	@EmbeddedId
	private DiagnosticReportFhirObservationGroupPK pk;

	public DiagnosticReportFhirObservationGroup(Integer fhirObservationGroupId, Integer diagnosticReportId) {
		this.pk = new DiagnosticReportFhirObservationGroupPK(fhirObservationGroupId,diagnosticReportId);
	}


}
