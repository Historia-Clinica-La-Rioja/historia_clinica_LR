package net.pladema.medication.infrastructure.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "article_drug", schema = "commercial_medication")
@Entity
public class CommercialMedicationArticleDrug implements Serializable {

	private static final long serialVersionUID = 1297941949126337575L;

	@EmbeddedId
	private CommercialMedicationArticleDrugPK pk;

	@Column(name = "potency")
	private Float potency;

	@Column(name = "potency_id")
	private Integer potencyId;

}
