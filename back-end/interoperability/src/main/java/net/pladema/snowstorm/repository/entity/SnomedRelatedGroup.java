package net.pladema.snowstorm.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "snomed_related_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SnomedRelatedGroup {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "snomed_id", nullable = false)
	private Integer snomedId;

	@Column(name = "group_id", nullable = false)
	private Integer groupId;

	@Column(name = "orden", nullable = false)
	private Integer orden;

	@Column(name = "last_update", nullable = false)
	private LocalDate lastUpdate;

	public SnomedRelatedGroup(Integer snomedId, Integer groupId, Integer orden, LocalDate lastUpdate) {
		this.snomedId = snomedId;
		this.groupId = groupId;
		this.orden = orden;
		this.lastUpdate = lastUpdate;
	}

	public SnomedRelatedGroup(Integer snomedId, Integer groupId) {
		this.snomedId = snomedId;
		this.groupId = groupId;
	}
}
