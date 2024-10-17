package net.pladema.medication.infrastructure.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.medication.domain.commercialMedicationArticle.VademecumAndCoverage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;

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
	private BigDecimal pamiAmbulatoryPercentage;

	@Column(name = "pami_sell_price")
	private BigDecimal pamiSellPrice;

	@Column(name = "sifar_coverage_brand")
	private String sifarCoverageBrand;

	@Column(name = "ioma_fixed_value")
	private BigDecimal iomaFixedValue;

	@Column(name = "standardized_use_brand")
	private String standardizedUseBrand;

	public CommercialMedicationArticleCoverage(Integer articleId, VademecumAndCoverage vademecumAndCoverage) {
		this.articleId = articleId;
		this.pamiAmbulatoryPercentage = vademecumAndCoverage.getC1();
		this.pamiSellPrice = vademecumAndCoverage.getC2();
		this.sifarCoverageBrand = vademecumAndCoverage.getC3();
		this.iomaFixedValue = vademecumAndCoverage.getC4();
		this.standardizedUseBrand = vademecumAndCoverage.getC5();
	}
}
