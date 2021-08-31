package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "document_file")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentFile extends SGXAuditableEntity<Long> {

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
						String filepath, String filename, String uuidFile, String checksum){
		this.id = documentId;
		this.sourceId = sourceId;
		this.sourceTypeId = sourceTypeId;
		this.typeId = documentType;
		this.filepath = filepath;
		this.filename = filename;
		this.uuidfile = uuidFile;
		this.checksum = checksum;
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
