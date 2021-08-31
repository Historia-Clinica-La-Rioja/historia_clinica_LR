package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "document_medicamention_statement")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentMedicamentionStatement implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@EmbeddedId
	private DocumentMedicamentionStatementPK pk;

	public DocumentMedicamentionStatement(Long documentId, Integer medicationStatementId){
		pk = new DocumentMedicamentionStatementPK(documentId, medicationStatementId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentMedicamentionStatement that = (DocumentMedicamentionStatement) o;
		return Objects.equals(pk, that.pk);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pk);
	}

    public Long getDocumentId() {
		return pk.getDocumentId();
    }
}
