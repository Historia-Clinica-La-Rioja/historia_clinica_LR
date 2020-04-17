package net.pladema.internation.repository.core.entity;

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
@Table(name = "document_medicament_statement")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentMedicamentStatment implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@EmbeddedId
	private DocumentMedicamentStatmentPK pk;

	public DocumentMedicamentStatment(Long documentId, Integer medicationStatementId){
		pk = new DocumentMedicamentStatmentPK(documentId, medicationStatementId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentMedicamentStatment that = (DocumentMedicamentStatment) o;
		return Objects.equals(pk, that.pk);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pk);
	}
}
