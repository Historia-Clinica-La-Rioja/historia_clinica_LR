package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "document_isolation_alert")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentIsolationAlert {
	@EmbeddedId
	DocumentIsolationAlertPK pk;
	public DocumentIsolationAlert(Long documentId, Integer isolationAlertId) {
		this.setPk(new DocumentIsolationAlertPK(documentId, isolationAlertId));
	}
}
