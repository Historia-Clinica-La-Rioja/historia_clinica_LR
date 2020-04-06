package net.pladema.address.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "department")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2775496112608602123L;

	@Id
	@Column(name = "id")
	private Short id;

	@Column(name = "description", length = 100, nullable = false)
	private String description;

	@Column(name = "province_id", nullable = false)
	private Short provinceId;

}
