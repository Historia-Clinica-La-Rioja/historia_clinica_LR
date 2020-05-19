package net.pladema.internation.repository.core.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.listener.InternationAuditableEntity;
import net.pladema.internation.repository.listener.InternationListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "document_file")
@EntityListeners(InternationListener.class)
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

	@Column(name = "file_path", length = 100, nullable = false)
	private String filepath;

	@Column(name = "file_name", length = 40, nullable = false)
	private String filename;

	@Column(name = "uuid_file", length = 36, nullable = false)
	private String uuidfile;

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
