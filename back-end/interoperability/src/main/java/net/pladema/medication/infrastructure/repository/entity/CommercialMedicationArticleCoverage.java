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
@Table(name = "article_coverage", schema = "commercial_medication")
@Entity
public class CommercialMedicationArticleCoverage implements Serializable {

	private static final long serialVersionUID = -5438625307363183718L;

	@Id
	@Column(name = "article_id")
	private Integer articleId;

	@Column(name = "pami_ambulatory_percentage")
	private Float pamiAmbulatoryPercentage;

	@Column(name = "pami_sell_price")
	private Float pamiSellPrice;

	@Column(name = "sifar_coverage_brand")
	private String sifarCoverageBrand;

	@Column(name = "ioma_fixed_value")
	private Float iomaFixedValue;

	@Column(name = "standardized_use_brand")
	private String standardizedUseBrand;

}
