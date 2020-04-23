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
@Table(name = "city")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class City implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2775496112608602123L;
	public static final char BARRIO = 'B';

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "description", length = 100, nullable = false)
	private String description;

	@Column(name = "department_id", nullable = false)
	private Short departmentId;

	@Column(name = "city_type")
	private Character cityType;

	@Column(name = "active")
	private Boolean active;

}
