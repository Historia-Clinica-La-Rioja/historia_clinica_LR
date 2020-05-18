package net.pladema.internation.repository.core.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.listener.InternationAuditableEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "document_file")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentFile extends InternationAuditableEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "internment_episode_id", nullable = false)
	private Integer internmentEpisodeId;

	@Column(name = "type_id", nullable = false)
	private Short typeId;

	@Column(name = "file_path", length = 60, nullable = false)
	private String filepath;

	@Column(name = "file_name", length = 20, nullable = false)
	private String filename;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentFile that = (DocumentFile) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
