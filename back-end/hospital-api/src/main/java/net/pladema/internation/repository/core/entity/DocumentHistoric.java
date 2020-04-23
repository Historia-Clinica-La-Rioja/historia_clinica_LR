package net.pladema.internation.repository.core.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "document_historic")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentHistoric {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@EmbeddedId
	private DocumentHistoricPK pk;

	@Column(name = "internment_episode_id", nullable = false)
	private Integer internmentEpisodeId;

	@Column(name = "status_id", length = 20, nullable = false)
	private String statusId;

	@Column(name = "note_id")
	private Long noteId;

	@Column(name = "type_id", nullable = false)
	private Short typeId;


	public DocumentHistoric(Long id, LocalDateTime createdOn){
		pk = new DocumentHistoricPK(id, createdOn);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentHistoric that = (DocumentHistoric) o;
		return Objects.equals(pk, that.pk);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pk);
	}
}
