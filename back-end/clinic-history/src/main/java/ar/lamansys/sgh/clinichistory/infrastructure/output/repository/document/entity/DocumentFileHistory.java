package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "document_file_history")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DocumentFileHistory extends SGXAuditableEntity<Long> {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "source_id", nullable = false)
	private Integer sourceId;

	@Column(name = "source_type_id", nullable = false)
	private Short sourceTypeId;

	@Column(name = "type_id", nullable = false)
	private Short typeId;

	@Column(name = "file_path", length = 200, nullable = false)
	private String filepath;

	@Column(name = "file_name", length = 60, nullable = false)
	private String filename;

	@Column(name = "uuid_file", length = 36, nullable = false)
	private String uuidfile;

	@Column(name = "checksum", length = 512)
	private String checksum;

	@Column(name = "signature_status_id", nullable = false)
	private Short signatureStatusId;

	public DocumentFileHistory(Long documentId, Integer sourceId, Short sourceTypeId, Short documentType,
						String filepath, String filename, String uuidFile, String checksum, Short signatureStatusId){
		this.documentId = documentId;
		this.sourceId = sourceId;
		this.sourceTypeId = sourceTypeId;
		this.typeId = documentType;
		this.filepath = filepath;
		this.filename = filename;
		this.uuidfile = uuidFile;
		this.checksum = checksum;
		this.signatureStatusId = signatureStatusId;
	}
	public DocumentFileHistory(DocumentFile df) {
		this.documentId = df.getId();
		this.sourceId = df.getSourceId();
		this.sourceTypeId = df.getSourceTypeId();
		this.typeId = df.getTypeId();
		this.filepath = df.getFilepath();
		this.filename = df.getFilename();
		this.uuidfile = df.getUuidfile();
		this.checksum = df.getChecksum();
		this.signatureStatusId = df.getSignatureStatusId();
	}

}
