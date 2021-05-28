package net.pladema.clinichistory.hospitalization.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "evolution_note_document")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EvolutionNoteDocument implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@EmbeddedId
	private EvolutionNoteDocumentPK pk;

	public EvolutionNoteDocument(Long documentId, Integer internmentEpisodeId){
		pk = new EvolutionNoteDocumentPK(documentId, internmentEpisodeId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EvolutionNoteDocument that = (EvolutionNoteDocument) o;
		return Objects.equals(pk, that.pk);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pk);
	}
}
