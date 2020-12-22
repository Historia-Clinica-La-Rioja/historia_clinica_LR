package net.pladema.emergencycare.triage.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "triage_category")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TriageCategory implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -5163231126045947686L;

	@Id
	@Column(name = "id")
	private Short id;

	@Column(name = "name", length = 10, nullable = false)
	private String name;

	@Column(name = "description", length = 15, nullable = false)
	private String description;

	@Column(name = "color_name", length = 10, nullable = false)
	private String colorName;

	@Column(name = "color_code", length = 8, nullable = false)
	private String colorCode;
}
