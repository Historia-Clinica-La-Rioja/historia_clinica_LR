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
@Table(name = "body_temperature")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BodyTemperature implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3027224088744597339L;

	@Id
	@Column(name = "id")
	private Short id;

	@Column(name = "description", length = 15, nullable = false)
	private String description;

	@Column(name = "snomed_id", nullable = false)
	private Integer snomedId;
}