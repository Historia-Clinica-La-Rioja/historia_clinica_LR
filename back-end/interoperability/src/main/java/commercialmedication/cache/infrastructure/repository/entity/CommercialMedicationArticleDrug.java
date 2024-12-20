package commercialmedication.cache.infrastructure.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import commercialmedication.cache.domain.commercialMedicationArticle.NewDrug;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;

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
	private BigDecimal potency;

	@Column(name = "potency_id")
	private Integer potencyId;

	public CommercialMedicationArticleDrug(Integer articleId, NewDrug drug) {
		this.pk = new CommercialMedicationArticleDrugPK(articleId, drug.getId());
		this.potency = drug.getPotency();
		this.potencyId = drug.getPotencyUnityId();
	}
}
