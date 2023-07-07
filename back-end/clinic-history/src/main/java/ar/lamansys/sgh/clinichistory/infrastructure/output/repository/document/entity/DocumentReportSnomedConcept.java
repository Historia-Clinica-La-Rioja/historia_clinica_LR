package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

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
public class DocumentReportSnomedConcept {

	@EmbeddedId
	private DocumentReportSnomedConceptPK pk;

	public DocumentReportSnomedConcept(Long documentId, Integer snomedId){
		pk = new DocumentReportSnomedConceptPK(documentId, snomedId);
	}
}
