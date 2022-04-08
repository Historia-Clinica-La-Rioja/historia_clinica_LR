package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "document_indication")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentIndication implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@EmbeddedId
	private DocumentIndicationPK pk;

	public DocumentIndication(Long documentId, Integer indicationId) {
		pk = new DocumentIndicationPK(documentId, indicationId);
	}
}
