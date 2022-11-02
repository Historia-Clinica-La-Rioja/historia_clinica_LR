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
@Table(name = "file_info_error")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileErrorInfo extends SGXAuditableEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "relative_path", nullable = false)
    private String relativePath;

    @Column(name = "error", nullable = false)
    private String error;

	@Column(name = "node_id", nullable = false)
	public String nodeId;

	public FileErrorInfo(String relativePath, String error, String nodeId) {
		this.relativePath = relativePath;
		this.error = error;
		this.nodeId = nodeId;
	}
}
