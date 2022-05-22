package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DocumentIndicationPK implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "indication_id", nullable = false)
	private Integer indicationId;
	
}
