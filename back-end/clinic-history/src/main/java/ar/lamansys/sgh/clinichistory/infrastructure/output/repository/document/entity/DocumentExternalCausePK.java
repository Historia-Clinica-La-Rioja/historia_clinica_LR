package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
public class DocumentExternalCausePK implements Serializable {

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "external_cause_id", nullable = false)
	private Integer externalCauseId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentExternalCausePK that = (DocumentExternalCausePK) o;
		return documentId.equals(that.documentId) &&
				externalCauseId.equals(that.externalCauseId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(documentId, externalCauseId);
	}
}
