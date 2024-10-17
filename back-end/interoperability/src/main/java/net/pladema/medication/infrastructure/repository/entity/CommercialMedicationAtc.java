package net.pladema.medication.infrastructure.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "atc", schema = "commercial_medication")
@Entity
public class CommercialMedicationAtc implements Serializable {

	private static final long serialVersionUID = 7300621797988689110L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "description")
	private String description;

	@Column(name = "comment")
	private String comment;

	@Column(name = "daily_defined_dose")
	private Float dailyDefinedDose;

	@Column(name = "unit")
	private String unit;

	@Column(name = "administration_via", length = 1)
	private String administrationVia;

	@Column(name = "daily_defined_dose_comment")
	private String dailyDefinedDoseComment;

	@Column(name = "level")
	private String level;

}
