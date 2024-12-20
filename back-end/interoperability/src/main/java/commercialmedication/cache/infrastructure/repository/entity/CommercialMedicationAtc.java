package commercialmedication.cache.infrastructure.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import commercialmedication.cache.domain.decodedResponse.ATCDetail;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;

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
	private BigDecimal dailyDefinedDose;

	@Column(name = "unit")
	private String unit;

	@Column(name = "administration_via")
	private String administrationVia;

	@Column(name = "daily_defined_dose_comment")
	private String dailyDefinedDoseComment;

	@Column(name = "level")
	private String level;

	public CommercialMedicationAtc(ATCDetail atc) {
		this.id = atc.getCode();
		this.description = atc.getDescription();
		this.comment = atc.getComment();
		this.dailyDefinedDose = atc.getDailyDose();
		this.unit = atc.getUnit();
		this.administrationVia = atc.getVia();
		this.dailyDefinedDoseComment = atc.getDddComment();
		this.level = atc.getLevel();
	}
}
