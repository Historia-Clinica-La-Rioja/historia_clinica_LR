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
@Table(name = "document_inmunization")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentInmunization implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@EmbeddedId
	private DocumentInmunizationPK pk;

	public DocumentInmunization(Long documentId, Integer inmunizationId){
		pk = new DocumentInmunizationPK(documentId, inmunizationId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentInmunization that = (DocumentInmunization) o;
		return Objects.equals(pk, that.pk);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pk);
	}
}
