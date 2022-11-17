package net.pladema.snowstorm.repository.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "snomed_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SnomedGroup {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "description", length = 100, nullable = false)
	private String description;

	@Column(name = "ecl", length = 255, nullable = false)
	private String ecl;

	@Column(name = "custom_id", length = 50, nullable = false)
	private String customId;

	@Column(name = "last_update", nullable = false)
	private LocalDate lastUpdate;

	@Column(name = "institution_id")
	private Integer institutionId;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "groupId")
	private Integer groupId;

	@Column(name = "template", nullable = false)
	private Boolean template = false;

	@Column(name = "group_type", nullable = false)
	private Short groupType;

	public SnomedGroup(Integer id, String description, String ecl, String customId, LocalDate lastUpdate, Short groupType) {
		this.id = id;
		this.description = description;
		this.ecl = ecl;
		this.customId = customId;
		this.lastUpdate = lastUpdate;
		this.groupType = groupType;
	}

}
