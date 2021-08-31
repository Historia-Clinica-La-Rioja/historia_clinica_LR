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
@Table(name = "document_allergy_intolerance")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentAllergyIntolerance implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@EmbeddedId
	private DocumentAllergyIntolerancePK pk;

	public DocumentAllergyIntolerance(Long documentId, Integer allergyIntoleranceId){
		pk = new DocumentAllergyIntolerancePK(documentId, allergyIntoleranceId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentAllergyIntolerance that = (DocumentAllergyIntolerance) o;
		return Objects.equals(pk, that.pk);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pk);
	}
}
