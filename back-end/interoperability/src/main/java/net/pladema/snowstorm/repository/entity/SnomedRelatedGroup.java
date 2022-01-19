package net.pladema.snowstorm.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "snomed_related_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SnomedRelatedGroup {

	@EmbeddedId
	private SnomedRelatedGroupPK pk;

	@Column(name = "orden", nullable = false)
	private Integer orden;

	@Column(name = "last_update", nullable = false)
	private LocalDate lastUpdate;

	public SnomedRelatedGroup(Integer snomedId, Integer groupId, Integer orden, LocalDate lastUpdate) {
		pk = new SnomedRelatedGroupPK(snomedId, groupId);
		this.orden = orden;
		this.lastUpdate = lastUpdate;
	}

	public Integer getSnomedId() {
		return pk.getSnomedId();
	}

	public Integer getGroupId() {
		return pk.getGroupId();
	}

}
