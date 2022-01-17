package net.pladema.snowstorm.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

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

	public SnomedRelatedGroup(Integer snomedId, Integer groupId, Integer orden) {
		pk = new SnomedRelatedGroupPK(snomedId, groupId);
		this.orden = orden;
	}

	public Integer getSnomedId() {
		return pk.getSnomedId();
	}

	public Integer getGroupId() {
		return pk.getGroupId();
	}

}
