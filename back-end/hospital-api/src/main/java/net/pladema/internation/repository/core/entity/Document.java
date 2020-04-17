package net.pladema.internation.repository.core.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.auditable.entity.AuditableEntity;
import net.pladema.auditable.listener.AuditListener;
import net.pladema.internation.repository.listener.InternationAuditableEntity;
import net.pladema.internation.repository.listener.InternationListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "document")
@EntityListeners(InternationListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Document extends InternationAuditableEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "internment_episode_id", nullable = false)
	private Integer internmentEpisodeId;

	@Column(name = "status_id", length = 20, nullable = false)
	private String statusId;

	@Column(name = "note_id")
	private Long noteId;

	@Column(name = "type_id", nullable = false)
	private Short typeId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Document document = (Document) o;
		return id.equals(document.id) &&
				internmentEpisodeId.equals(document.internmentEpisodeId) &&
				typeId.equals(document.typeId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, internmentEpisodeId, typeId);
	}
}
