package net.pladema.clinichistory.documents.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.listener.InternationAuditableEntity;
import net.pladema.clinichistory.hospitalization.repository.listener.InternationListener;

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

	@Column(name = "source_id", nullable = false)
	private Integer sourceId;

	@Column(name = "source_type_id", nullable = false)
	private Short sourceTypeId;

	@Column(name = "type_id", nullable = false)
	private Short typeId;

	@Column(name = "file_path", length = 200, nullable = false)
	private String filepath;

	@Column(name = "file_name", length = 40, nullable = false)
	private String filename;

	@Column(name = "uuid_file", length = 36, nullable = false)
	private String uuidfile;

	@Column(name = "checksum", length = 512)
	private String checksum;

	public DocumentFile(Long documentId, Integer sourceId, Short sourceTypeId, Short documentType,
						String filepath, String filename, String uuidFile){
		this.id = documentId;
		this.sourceId = sourceId;
		this.sourceTypeId = sourceTypeId;
		this.typeId = documentType;
		this.filepath = filepath;
		this.filename = filename;
		this.uuidfile = uuidFile;
	}

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
