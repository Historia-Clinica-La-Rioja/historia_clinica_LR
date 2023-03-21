package ar.lamansys.sgx.shared.files.infrastructure.output.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "file_info")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileInfo extends SGXAuditableEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name="name", nullable = false)
	private String name;

    @Column(name = "relative_path")
    private String relativePath;

	@Column(name = "original_path")
	private String originalPath;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name="size", nullable = false)
    private Long size;

	@Column(name = "uuid_file", length = 36)
	private String uuidfile;

	@Column(name = "checksum", length = 512)
	private String checksum;

	/**
	 * <b>Note:</b> Guarda la fuente de información de la cuál se creo esta entrada en la base de datos
	 *
	 * @author  Trapani Ezequiel
	 * @version 1.46.0
	 * @since   2022-11-10
	 */
	@Column(name = "source")
	private String source;
	/**
	 * <b>Note:</b> Guarda la forma en que se genero esta entrada: por el sistema, por una consulta externa, etc
	 *
	 * @author  Trapani Ezequiel
	 * @version 1.46.0
	 * @since   2022-11-10
	 */
	@Column(name = "generated_by")
	private String generatedBy;


	public FileInfo(String name, String relativePath,
					String contentType, long size, String uuidfile,
					String checksum, String source) {
		this.name = name;
		this.relativePath = relativePath;
		this.originalPath = relativePath;
		this.contentType = contentType;
		this.size = size;
		this.uuidfile = uuidfile;
		this.checksum = checksum;
		this.source = source;
		this.generatedBy = "SISTEMA";
	}
}
