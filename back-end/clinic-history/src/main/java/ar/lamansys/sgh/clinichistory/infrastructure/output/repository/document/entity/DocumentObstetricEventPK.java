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
public class DocumentObstetricEventPK implements Serializable {

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "obstetric_event_id", nullable = false)
	private Integer obstetricEventId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentObstetricEventPK that = (DocumentObstetricEventPK) o;
		return documentId.equals(that.documentId) &&
				obstetricEventId.equals(that.obstetricEventId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(documentId, obstetricEventId);
	}

}
