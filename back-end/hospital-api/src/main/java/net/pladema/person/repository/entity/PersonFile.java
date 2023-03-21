package net.pladema.person.repository.entity;

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
@Table(name = "person_file")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PersonFile extends SGXAuditableEntity<Integer> {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "path", nullable = false)
	private String path;

	@Column(name = "content_type", nullable = false)
	private String contentType;

	@Column(name="size", nullable = false)
	private long size;

	@Column(name="name", nullable = false)
	private String name;

	@Column(name="person_id", nullable = false)
	private Integer personId;

	@Column(name="institution_id", nullable = false)
	private Integer institutionId;

	public PersonFile(String path, String contentType, long size, String name, Integer institutionId, Integer personId){
		super();
		this.path = path;
		this.contentType = contentType;
		this.size = size;
		this.name = name;
		this.institutionId = institutionId;
		this.personId = personId;
	}
}
