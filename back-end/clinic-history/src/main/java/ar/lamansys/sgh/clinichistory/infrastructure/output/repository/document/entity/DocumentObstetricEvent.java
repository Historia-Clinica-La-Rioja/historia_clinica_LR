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
@Table(name = "document_obstetric_event")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentObstetricEvent {

	@EmbeddedId
	private DocumentObstetricEventPK pk;

	public DocumentObstetricEvent(Long documentId, Integer obstetricEventId){
		pk = new DocumentObstetricEventPK(documentId, obstetricEventId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentObstetricEvent that = (DocumentObstetricEvent) o;
		return Objects.equals(pk, that.pk);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pk);
	}

}
