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
@Table(name = "document_vital_sign")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentVitalSign implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@EmbeddedId
	private DocumentVitalSignPK pk;

	public DocumentVitalSign(Long documentId, Integer observationVitalSignId){
		pk = new DocumentVitalSignPK(documentId, observationVitalSignId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentVitalSign that = (DocumentVitalSign) o;
		return Objects.equals(pk, that.pk);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pk);
	}
}
