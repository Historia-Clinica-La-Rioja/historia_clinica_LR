package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DocumentReferableConceptPK implements Serializable {

	private static final long serialVersionUID = -723390480008211077L;

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "referable_concept_id", nullable = false)
	private Short referableConceptId;

}
