package net.pladema.internation.repository.core.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "evaluation_note_document")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EvaluationNoteDocument implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@EmbeddedId
	private EvaluationNoteDocumentPK pk;

	public EvaluationNoteDocument(Long documentId, Integer internmentEpisodeId){
		pk = new EvaluationNoteDocumentPK(documentId, internmentEpisodeId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EvaluationNoteDocument that = (EvaluationNoteDocument) o;
		return Objects.equals(pk, that.pk);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pk);
	}
}
