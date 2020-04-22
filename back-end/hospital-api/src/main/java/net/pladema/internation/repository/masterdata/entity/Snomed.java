package net.pladema.internation.repository.masterdata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "snomed")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Snomed implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id", length = 20)
	private String id;

	@Column(name = "fsn", nullable = false, length = 100)
	private String fsn;

	@Column(name = "parent_id", length = 20)
	private String parentId;

	@Column(name = "parent_fsn", nullable = false, length = 100)
	private String parentFsn;
}
