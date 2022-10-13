package net.pladema.snowstorm.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Immutable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Immutable
@Table(name = "v_snomed_group_concept")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VSnomedGroupConcept {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "concept_id")
	private Integer conceptId;

	@Column(name = "group_id")
	private Integer groupId;

	@Column(name = "concept_sctid")
	private String conceptSctid;

	@Column(name = "concept_pt")
	private String conceptPt;

	@Column(name = "orden")
	private Integer orden;

	@Column(name = "last_update")
	private LocalDate lastUpdate;

	public VSnomedGroupConcept(Integer id, String conceptPt) {
		this.id = id;
		this.conceptPt = conceptPt;
	}

}
