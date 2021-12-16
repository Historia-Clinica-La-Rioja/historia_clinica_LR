package net.pladema.snowstorm.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "snomed_related_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SnomedRelatedGroup {

	@Id
	@Column(name = "snomed_id", nullable = false)
	private Integer snomedId;

	@Column(name = "group_id", nullable = false)
	private Integer groupId;

	@Column(name = "orden", nullable = false)
	private Integer orden;
}
