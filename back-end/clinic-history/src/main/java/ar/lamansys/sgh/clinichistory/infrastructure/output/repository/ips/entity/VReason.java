package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "v_reasons")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VReason implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id", length = 20)
	private String id;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "source_id", nullable = false)
	private Integer sourceId;

	@Column(name = "document_type_id", nullable = false)
	private Short documentTypeId;

	@Column(name = "source_type_id", nullable = false)
	private Short sourceTypeId;

}
