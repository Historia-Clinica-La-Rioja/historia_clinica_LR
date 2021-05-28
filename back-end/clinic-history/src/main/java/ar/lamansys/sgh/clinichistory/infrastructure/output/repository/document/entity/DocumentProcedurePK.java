package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DocumentProcedurePK implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "procedure_id", nullable = false)
	private Integer procedureId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentProcedurePK that = (DocumentProcedurePK) o;
		return documentId.equals(that.documentId) &&
				procedureId.equals(that.procedureId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(documentId, procedureId);
	}
}
