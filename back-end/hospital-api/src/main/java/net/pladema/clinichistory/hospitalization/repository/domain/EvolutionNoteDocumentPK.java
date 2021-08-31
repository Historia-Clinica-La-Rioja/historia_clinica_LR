package net.pladema.clinichistory.hospitalization.repository.domain;

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
public class EvolutionNoteDocumentPK implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "internment_episode_id", nullable = false)
	private Integer internmentEpisodeId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EvolutionNoteDocumentPK that = (EvolutionNoteDocumentPK) o;
		return documentId.equals(that.documentId) &&
				internmentEpisodeId.equals(that.internmentEpisodeId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(documentId, internmentEpisodeId);
	}
}
