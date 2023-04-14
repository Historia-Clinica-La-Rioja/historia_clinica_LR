package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DocumentTriagePK implements Serializable {

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "triage_id", nullable = false)
	private Integer triageId;


}
