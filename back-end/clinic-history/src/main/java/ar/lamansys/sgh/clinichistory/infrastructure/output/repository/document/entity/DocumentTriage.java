package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentTriagePK;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "document_triage")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentTriage implements Serializable {

	@EmbeddedId
	private DocumentTriagePK pk;

	public DocumentTriage(Long documentId, Integer triageId){
		pk = new DocumentTriagePK(documentId, triageId);
	}

}
