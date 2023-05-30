package net.pladema.clinichistory.requests.servicerequests.infrastructure.output.ReportSnomedConcept;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "report_snomed_concepts")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReportSnomedConcept {

	@EmbeddedId
	private DocumentReportSnomedConceptPK pk;

	public ReportSnomedConcept(Long documentId, Integer snomedId){
		pk = new DocumentReportSnomedConceptPK(documentId, snomedId);
	}
}