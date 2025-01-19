package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "document_referable_concept")
@Entity
public class DocumentReferableConcept {

	@EmbeddedId
	private DocumentReferableConceptPK pk;

	@Column(name = "is_referred")
	private boolean isReferred;

}
