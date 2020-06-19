package net.pladema.clinichistory.documents.repository.entity;

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
public class DocumentVitalSignPK implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "observation_vital_sign_id", nullable = false)
	private Integer observationVitalSignId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentVitalSignPK that = (DocumentVitalSignPK) o;
		return documentId.equals(that.documentId) &&
				observationVitalSignId.equals(that.observationVitalSignId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(documentId, observationVitalSignId);
	}
}
