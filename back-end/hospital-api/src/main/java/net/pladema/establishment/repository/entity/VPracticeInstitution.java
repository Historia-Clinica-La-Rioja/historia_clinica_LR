package net.pladema.establishment.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Immutable
@Table(name = "v_practice_institution")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VPracticeInstitution {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

	@Column(name = "pt", nullable = false)
	private String description;
}
