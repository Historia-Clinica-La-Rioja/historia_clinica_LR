package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "document_external_cause")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentExternalCause {

	private static final long serialVersionUID = -3053291021636483828L;

	@EmbeddedId
	private DocumentExternalCausePK pk;

	public DocumentExternalCause(Long documentId, Integer externalCauseId){
		pk = new DocumentExternalCausePK(documentId, externalCauseId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentExternalCause that = (DocumentExternalCause) o;
		return Objects.equals(pk, that.pk);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pk);
	}

}
