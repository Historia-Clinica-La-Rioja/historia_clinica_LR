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
public class DocumentLabPK implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "observation_lab_id", nullable = false)
	private Integer observationLabId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentLabPK that = (DocumentLabPK) o;
		return documentId.equals(that.documentId) &&
				observationLabId.equals(that.observationLabId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(documentId, observationLabId);
	}
}
