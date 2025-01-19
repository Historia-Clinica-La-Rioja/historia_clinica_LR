package net.pladema.template.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "document_template")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentTemplate {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Integer userId;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "type_id", nullable = false)
	private Short typeId;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "file_id", nullable = false)
	private Long fileId;

	@Column(name = "deleted", nullable = false)
	private Boolean deleted;
}
